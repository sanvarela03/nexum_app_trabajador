package com.example.nexum_trabajador.data.message.remote.websocket

import android.util.Log
import com.example.nexum_trabajador.data.message.remote.payload.req.MessageRequest
import com.example.nexum_trabajador.domain.model.ConnectionState
import com.example.nexum_trabajador.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 2.1
 */
@Singleton
class WebSocketManager @Inject constructor(
    private val httpClient: HttpClient,
    private val json: Json
) {
    companion object {
        private const val TAG = "WebSocketManager"
        private const val MAX_RECONNECT_ATTEMPTS = 10
        private const val INITIAL_RECONNECT_DELAY = 1000L
        private const val MAX_RECONNECT_DELAY = 30000L
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connectionMutex = Mutex()

    private var webSocketSession: DefaultClientWebSocketSession? = null
    private var connectionJob: Job? = null
    private var reconnectJob: Job? = null
    private var heartbeatJob: Job? = null
    private val subscriptionId = AtomicInteger(0)

    private var subscribed = false
    private var reconnectAttempts = 0
    private var manuallyDisconnected = false
    private var currentServerUrl: String? = null
    private var currentJwtToken: String? = null
    
    private var tokenProvider: (suspend () -> String?)? = null

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _messages = MutableSharedFlow<Message>(extraBufferCapacity = 64)
    val messages: SharedFlow<Message> = _messages.asSharedFlow()

    // Flujo de eventos para notificaciones globales
    val webSocketEvents = MutableSharedFlow<WebSocketEvent>()

    fun setTokenProvider(provider: suspend () -> String?) {
        this.tokenProvider = provider
    }

    suspend fun connect(serverUrl: String, jwtToken: String) = connectionMutex.withLock {
        reconnectJob?.cancel()
        reconnectJob = null

        currentServerUrl = serverUrl
        currentJwtToken = jwtToken
        manuallyDisconnected = false

        if (connectionJob?.isActive == true) return@withLock

        _connectionState.value = ConnectionState.CONNECTING

        connectionJob = scope.launch {
            var exitState = ConnectionState.DISCONNECTED
            try {
                httpClient.webSocket(urlString = "$serverUrl/ws/websocket") {
                    webSocketSession = this
                    Log.d(TAG, "✅ WebSocket connected")

                    sendStompConnect(jwtToken)

                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> handleIncomingFrame(frame.readText())
                            is Frame.Close -> Log.d(TAG, "🔌 Connection closed by server")
                            else -> {
                                Log.d(TAG, "IDK BRO 🫠")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                if (e !is ClosedReceiveChannelException && e !is CancellationException) {
                    Log.e(TAG, "❌ Connection/Session error: ${e.message}", e)
                    exitState = ConnectionState.ERROR
                }
            } finally {
                val sessionWasActive = webSocketSession != null
                if (sessionWasActive) {
                    cleanupSession(exitState)
                }

                if (sessionWasActive && !manuallyDisconnected && reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
                    attemptReconnect()
                }
            }
        }
    }

    private fun attemptReconnect() {
        if (reconnectJob?.isActive == true || manuallyDisconnected) return

        reconnectJob = scope.launch {
            _connectionState.value = ConnectionState.RECONNECTING

            reconnectAttempts++
            val exponentialDelay = INITIAL_RECONNECT_DELAY shl min(reconnectAttempts - 1, 10)
            val baseDelay = min(exponentialDelay, MAX_RECONNECT_DELAY)
            val jitter = (baseDelay * 0.1).toLong().let { if (it > 0) (0..it).random() else 0L }
            val delayMillis = min(baseDelay + jitter, MAX_RECONNECT_DELAY)

            Log.d(TAG, "🔄 Reconnecting in ${delayMillis}ms... (Attempt $reconnectAttempts/$MAX_RECONNECT_ATTEMPTS)")
            delay(delayMillis)

            currentServerUrl?.let { url ->
                val freshToken = tokenProvider?.invoke() ?: currentJwtToken
                if (freshToken != null) {
                    connect(url, freshToken)
                }
            }
        }
    }

    private fun cleanupSession(targetState: ConnectionState) {
        heartbeatJob?.cancel()
        heartbeatJob = null
        webSocketSession = null
        subscribed = false
        _connectionState.value = targetState
        Log.d(TAG, "🧹 Session cleaned. State: $targetState")
    }

    private suspend fun sendStompConnect(jwtToken: String) {
        val session = webSocketSession ?: run {
            throw IllegalStateException("Cannot send CONNECT frame: WebSocket session is null")
        }
        val connectFrame = StompFrame("CONNECT", mapOf(
            "Authorization" to "Bearer $jwtToken",
            "accept-version" to "1.2",
            "heart-beat" to "10000,10000"
        ))
        sendRaw(connectFrame.toRawString())
    }

    suspend fun subscribeToMessages() {
        if (_connectionState.value != ConnectionState.CONNECTED || subscribed) return
        
        val subIdMsg = subscriptionId.incrementAndGet().toString()
        safeSend(StompFrame("SUBSCRIBE", mapOf("id" to subIdMsg, "destination" to "/user/queue/messages")))
        
        val subIdRead = subscriptionId.incrementAndGet().toString()
        safeSend(StompFrame("SUBSCRIBE", mapOf("id" to subIdRead, "destination" to "/user/queue/read")))
        
        val subIdTyping = subscriptionId.incrementAndGet().toString()
        safeSend(StompFrame("SUBSCRIBE", mapOf("id" to subIdTyping, "destination" to "/user/queue/typing")))
        
        subscribed = true
        Log.d(TAG, "📥 Subscribed to messages (ID: $subIdMsg), read (ID: $subIdRead), typing (ID: $subIdTyping)")
    }

    suspend fun sendMessage(message: MessageRequest) {
        val frame = StompFrame("SEND", mapOf("destination" to "/app/chat.send", "content-type" to "application/json"), json.encodeToString(message))
        safeSend(frame)
    }

    suspend fun markAsRead(conversationId: String) {
        val payload = json.encodeToString(ReadPayload(conversationId))
        safeSend(StompFrame("SEND", mapOf("destination" to "/app/chat.read", "content-type" to "application/json"), payload))
    }

    suspend fun notifyTyping(conversationId: String, isTyping: Boolean) {
        val payload = json.encodeToString(TypingPayload(conversationId, isTyping))
        safeSend(StompFrame("SEND", mapOf("destination" to "/app/chat.typing", "content-type" to "application/json"), payload))
    }

    private suspend fun confirmDelivery(messageId: String) {
        val payload = json.encodeToString(DeliveryPayload(messageId))
        safeSend(StompFrame("SEND", mapOf("destination" to "/app/chat.delivered", "content-type" to "application/json"), payload))
    }

    private suspend fun safeSend(frame: StompFrame) {
        if (_connectionState.value != ConnectionState.CONNECTED) return
        sendRaw(frame.toRawString())
    }
    
    private suspend fun sendRaw(data: String) {
        try {
            webSocketSession?.send(Frame.Text(data))
        } catch (e: Exception) {
            Log.e(TAG, "Error sending raw data: ${e.message}")
        }
    }

    private suspend fun handleIncomingFrame(rawFrame: String) {
        val frame = StompFrame.fromRawString(rawFrame) ?: return
        when (frame.command) {
            "CONNECTED" -> {
                Log.d(TAG, "✅ STOMP connected")
                _connectionState.value = ConnectionState.CONNECTED
                reconnectAttempts = 0
                startHeartbeat()
                scope.launch { subscribeToMessages() }
            }
            "MESSAGE" -> {
                Log.d(TAG, "📥 INCOMING MESSAGE FRAME: ${frame.body}")
                try {
                    // Check if it's a Read receipt payload or a full Message
                    if (frame.body.contains("\"conversationId\"") && !frame.body.contains("\"senderId\"")) {
                        // Check if it's a TypingPayload
                        if (frame.body.contains("\"isTyping\"")) {
                            Log.d(TAG, "Detected Typing payload")
                            val typingPayload = json.decodeFromString<TypingPayload>(frame.body)
                            scope.launch {
                                webSocketEvents.emit(WebSocketEvent.TypingEvent(typingPayload.conversationId, typingPayload.isTyping))
                            }
                            return
                        }

                        // Otherwise it's likely a ReadPayload
                        Log.d(TAG, "Detected Read payload")
                        val readPayload = json.decodeFromString<ReadPayload>(frame.body)
                        scope.launch {
                            webSocketEvents.emit(WebSocketEvent.MessageRead(readPayload.conversationId))
                        }
                        return
                    }

                    val message = json.decodeFromString<Message>(frame.body)
                    
                    // Sometimes backends send status updates as the message itself.
                    // If the backend resends the message with status == READ, we should also trigger it.
                    if (message.status == com.example.nexum_trabajador.domain.model.MessageStatus.READ) {
                        scope.launch {
                            webSocketEvents.emit(WebSocketEvent.MessageRead(message.conversationId))
                        }
                    }

                    if (!_messages.tryEmit(message)) {
                        Log.w(TAG, "Message dropped: SharedFlow buffer full")
                    }
                    scope.launch { 
                        webSocketEvents.emit(WebSocketEvent.MessageReceived(message))
                        confirmDelivery(message.id)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing message: ${e.message}", e)
                }
            }
            "ERROR" -> _connectionState.value = ConnectionState.ERROR
        }
    }

    private fun startHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = scope.launch {
            while (true) {
                delay(10000)
                if (_connectionState.value == ConnectionState.CONNECTED) {
                    sendRaw("\n") // Correct STOMP heartbeat (just LF)
                }
            }
        }
        Log.d(TAG, "❤️ Heartbeat started")
    }

    suspend fun disconnect() {
        manuallyDisconnected = true
        reconnectJob?.cancel()
        reconnectJob = null
        if (webSocketSession != null) {
            try {
                safeSend(StompFrame("DISCONNECT"))
                delay(100)
                webSocketSession?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error during disconnect: ${e.message}")
            } finally {
                connectionJob?.cancel()
                cleanupSession(ConnectionState.DISCONNECTED)
                Log.d(TAG, "🔌 Disconnected manually")
            }
        }
    }

    fun destroy() {
        scope.cancel("WebSocketManager destroyed")
    }
}

// --- Eventos y Payloads ---

sealed class WebSocketEvent {
    data class MessageReceived(val message: Message) : WebSocketEvent()
    data class MessageRead(val conversationId: String) : WebSocketEvent()
    data class TypingEvent(val conversationId: String, val isTyping: Boolean) : WebSocketEvent()
}

@Serializable private data class ReadPayload(val conversationId: String)
@Serializable private data class TypingPayload(val conversationId: String, val isTyping: Boolean)
@Serializable private data class DeliveryPayload(val messageId: String)
