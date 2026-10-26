package com.example.nexum_trabajador.data.message

import android.util.Log
import com.example.nexum_trabajador.data.message.remote.api.MessagingApiService
import com.example.nexum_trabajador.data.message.remote.payload.req.MessageRequest
import com.example.nexum_trabajador.data.message.remote.websocket.WebSocketManager
import com.example.nexum_trabajador.domain.model.ConnectionState
import com.example.nexum_trabajador.domain.model.Conversation
import com.example.nexum_trabajador.domain.model.Message
import com.example.nexum_trabajador.domain.model.PageResponse
import com.example.nexum_trabajador.domain.repository.MessagingRepository
import com.example.nexum_trabajador.security.TokenManager
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagingRepositoryImpl @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val apiService: MessagingApiService,
    private val tokenManager: TokenManager
) : MessagingRepository {

    init {
        // Configuramos el provider para que intente obtener un token válido
        webSocketManager.setTokenProvider {
            ensureValidToken()
        }
    }

    /**
     * Esta función intenta obtener el token. 
     * Si el token está a punto de expirar o ya expiró, podríamos forzar 
     * una llamada REST simple (como getUnreadCount) para que el AuthAuthenticator actúe.
     */
    private suspend fun ensureValidToken(): String? {
        val currentToken = tokenManager.getAccessToken().firstOrNull()
        
        // Intentamos una petición ligera para disparar el Authenticator de OkHttp si el token es inválido
        return try {
            // Si el token falla, el Authenticator inyectado en apiService (vía OkHttpClient)
            // se encargará de refrescarlo en el DataStore.
            apiService.getUnreadCount(currentToken ?: "")
            // Después de la llamada (exitosa o refrescada), obtenemos el token actualizado
            tokenManager.getAccessToken().firstOrNull()
        } catch (e: Exception) {
            Log.e("MessagingRepository", "Error ensuring valid token, returning current", e)
            currentToken
        }
    }
    override val connectionState: SharedFlow<ConnectionState> = webSocketManager.connectionState
    override val incomingMessages: SharedFlow<Message?> = webSocketManager.messages
    override val webSocketEvents = webSocketManager.webSocketEvents

    override suspend fun connectWebSocket(serverUrl: String, jwtToken: String) {
        val validToken = ensureValidToken() ?: jwtToken
        webSocketManager.connect(serverUrl, validToken)
    }

    override suspend fun disconnectWebSocket() {
        webSocketManager.disconnect()
    }

    override suspend fun subscribeToMessages() {
        webSocketManager.subscribeToMessages()
    }

    override suspend fun sendMessageViaWebSocket(message: MessageRequest) {
        webSocketManager.sendMessage(message)
    }

    override suspend fun markAsReadViaWebSocket(conversationId: String) {
        webSocketManager.markAsRead(conversationId)
    }

    override suspend fun notifyTyping(conversationId: String, isTyping: Boolean) {
        webSocketManager.notifyTyping(conversationId, isTyping)
    }

    override suspend fun sendMessageViaRest(message: MessageRequest): Result<Message> {
        return try {
            val token = ensureValidToken() ?: return Result.failure(Exception("No token found"))
            val response = apiService.sendMessage(token, message)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConversations(page: Int, size: Int): Result<PageResponse<Conversation>> {
        return try {
            val token = ensureValidToken() ?: return Result.failure(Exception("No token found"))
            val response = apiService.getConversations(token, page, size)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConversationMessages(conversationId: String, page: Int, size: Int): Result<PageResponse<Message>> {
        return try {
            val token = ensureValidToken() ?: return Result.failure(Exception("No token found"))
            val response = apiService.getConversationMessages(token, conversationId, page, size)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAsReadViaRest(conversationId: String): Result<Unit> {
        return try {
            val token = ensureValidToken() ?: return Result.failure(Exception("No token found"))
            apiService.markAsRead(token, conversationId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUnreadCount(): Result<Long> {
        return try {
            val token = ensureValidToken() ?: return Result.failure(Exception("No token found"))
            val response = apiService.getUnreadCount(token)
            Result.success(response.unreadCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
