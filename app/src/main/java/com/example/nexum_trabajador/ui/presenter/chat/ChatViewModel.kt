package com.example.nexum_trabajador.ui.presenter.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.message.remote.payload.req.MessageRequest
import com.example.nexum_trabajador.data.message.remote.websocket.WebSocketEvent
import com.example.nexum_trabajador.domain.model.ConnectionState
import com.example.nexum_trabajador.domain.model.Message
import com.example.nexum_trabajador.domain.model.MessageStatus
import com.example.nexum_trabajador.domain.model.MessageType
import com.example.nexum_trabajador.domain.use_cases.chat.ChatUseCases
import com.example.nexum_trabajador.domain.use_cases.profile.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.nexum_trabajador.domain.model.Conversation
import com.example.nexum_trabajador.domain.use_cases.conversation.ConversationUseCases
import com.example.nexum_trabajador.security.TokenManager

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 2.0
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val chatUseCases: ChatUseCases,
    private val conversationUseCases: ConversationUseCases,
    private val tokenManager: TokenManager,
) : ViewModel() {
    companion object {
        private const val TAG = "ChatViewModel"
    }

    val currentUserId: String get() = tokenManager.getUserIdSync()?.toString() ?: ""

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val connectionState: SharedFlow<ConnectionState> = chatUseCases.getConnectionState()

    val profiles = profileUseCases.observeProfile()

    private val _isReceiverTyping = MutableStateFlow(false)
    val isReceiverTyping: StateFlow<Boolean> = _isReceiverTyping.asStateFlow()

    private var currentConversationId = MutableStateFlow<String?>(null)
    private var targetReceiverId: String? = null
    private var currentPage = 0
    private val pageSize = 50
    private var canLoadMore = true

    init {
        observeIncomingMessages()
    }

    fun connectWebSocket() {
        viewModelScope.launch {
            chatUseCases.connectWebSocket()
        }
    }

    fun disconnectWebSocket() {
        viewModelScope.launch {
            chatUseCases.disconnectWebSocket()
        }
    }

    fun loadConversationMessages(
        conversationId: String,
        receiverId: String? = null,
        loadMore: Boolean = false
    ) {
        if (!loadMore) {
            targetReceiverId = receiverId
            currentPage = 0
            canLoadMore = true
            _messages.value = emptyList()

            if (conversationId.isBlank() || conversationId == "new") {
                if (receiverId != null) {
                    viewModelScope.launch {
                        _uiState.value = ChatUiState.Loading
                        val existingConv = findExistingConversationWith(receiverId)
                        if (existingConv != null) {
                            Log.d(TAG, "Found existing conversation ${existingConv.id} for receiverId: $receiverId")
                            currentConversationId.value = existingConv.id
                            fetchMessages(existingConv.id, loadMore = false)
                        } else {
                            Log.d(TAG, "No existing conversation for receiverId: $receiverId. State -> Success")
                            currentConversationId.value = "new"
                            _uiState.value = ChatUiState.Success
                        }
                    }
                } else {
                    currentConversationId.value = "new"
                    _uiState.value = ChatUiState.Success
                }
                return
            }

            currentConversationId.value = conversationId
        }

        if (!canLoadMore) return

        val activeId = currentConversationId.value ?: conversationId
        if (activeId.isBlank() || activeId == "new") return

        viewModelScope.launch {
            fetchMessages(activeId, loadMore)
        }
    }

    private suspend fun findExistingConversationWith(receiverId: String): Conversation? {
        return try {
            val result = conversationUseCases.getConversations(page = 0, size = 50)
            result.getOrNull()?.content?.firstOrNull { conv ->
                conv.participantIds.contains(receiverId)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error looking up conversation for receiverId $receiverId", e)
            null
        }
    }

    private suspend fun fetchMessages(conversationId: String, loadMore: Boolean) {
        _uiState.value = if (loadMore) ChatUiState.LoadingMore else ChatUiState.Loading

        chatUseCases.loadConversationMessages(conversationId, currentPage, pageSize)
            .onSuccess { response ->
                val newMessages = if (loadMore) {
                    (_messages.value + response.content).distinctBy { it.id }
                } else {
                    response.content.distinctBy { it.id }
                }
                _messages.value = newMessages
                currentPage++
                canLoadMore = response.content.size == pageSize
                _uiState.value = ChatUiState.Success

                if (!loadMore) {
                    markAsRead()
                }
            }
            .onFailure { error ->
                Log.e(TAG, "Error loading messages", error)
                _uiState.value = ChatUiState.Error(error.message ?: "Error loading messages")
            }
    }

    fun fetchReceiverProfile(receiverId: String) {
        val userId = receiverId.toLongOrNull() ?: return
        viewModelScope.launch {
            profileUseCases.updateProfile(listOf(userId), fetchFromRemote = true).collect()
        }
    }

    fun sendMessage(
        receiverId: String,
        receiverRole: String,
        content: String,
        type: MessageType = MessageType.TEXT,
        metadata: Map<String, String>? = null,
        replyToMessageId: String? = null
    ) {
        viewModelScope.launch {
            // BUG FIX: Esperar a que el WebSocket esté realmente conectado

            if (chatUseCases.getConnectionState().first() != ConnectionState.CONNECTED) {
                connectWebSocket()
                // Esperar un máximo de 3 segundos a que conecte
                chatUseCases.getConnectionState().filter { it == ConnectionState.CONNECTED }.take(1)
                    .collect()
            }

            val request = MessageRequest(
                receiverId = receiverId,
                receiverRole = receiverRole,
                type = type,
                content = content,
                metadata = metadata,
                replyToMessageId = replyToMessageId
            )

            chatUseCases.sendMessage(request)
        }
    }

    fun markAsRead() {
        val id = currentConversationId.value
        if (id != null && id != "new") {
            viewModelScope.launch { chatUseCases.markMessageAsRead(id) }
        }
    }

    private var typingJob: Job? = null
    private var isCurrentlyTyping = false

    fun setTyping() {
        val id = currentConversationId.value

        Log.d(TAG, "setTyping: ${id == null || id == "new"}")

        if (id == null || id == "new") return

        typingJob?.cancel()

        if (!isCurrentlyTyping) {
            isCurrentlyTyping = true
            viewModelScope.launch {
                chatUseCases.notifyTyping(id, true)
            }
        }

        typingJob = viewModelScope.launch {
            delay(3000)
            isCurrentlyTyping = false
            chatUseCases.notifyTyping(id, false)
        }
    }

    private fun observeIncomingMessages() {
        // Observer for new incoming messages
        viewModelScope.launch {
            chatUseCases.getIncomingMessages().collect { message ->
                message?.let { msg ->
                    val activeId = currentConversationId.value
                    val receiverId = targetReceiverId

                    if (activeId != null && msg.conversationId == activeId) {
                        addMessageToList(msg)
                        markAsRead()
                    }
                    // BUG FIX: Transición de "new" a ID real
                    else if (activeId == "new" && receiverId != null) {
                        if (msg.senderId == receiverId || msg.receiverId == receiverId) {
                            Log.d(
                                "ChatViewModel",
                                "Switching from 'new' to real ID: ${msg.conversationId}"
                            )
                            currentConversationId.value = msg.conversationId
                            addMessageToList(msg)
                            markAsRead()
                        }
                    }
                }
            }
        }

        // Observer for global WebSocket events (like Read receipts)
        viewModelScope.launch {
            chatUseCases.getWebSocketEvents().collect { event ->
                val activeId = currentConversationId.value
                when (event) {
                    is WebSocketEvent.MessageRead -> {
                        if (activeId != null && event.conversationId == activeId) {
                            // Update all my sent messages to READ
                            val updatedMessages = _messages.value.map { msg ->
                                // If I am the sender, mark it as read when the other person reads the conversation
                                if (msg.senderId != targetReceiverId && msg.status != MessageStatus.READ) {
                                    msg.copy(status = MessageStatus.READ)
                                } else {
                                    msg
                                }
                            }
                            _messages.value = updatedMessages
                            Log.d(
                                "ChatViewModel",
                                "Updated read receipts locally for conversation: $activeId"
                            )
                        }
                    }

                    is WebSocketEvent.TypingEvent -> {
                        if (activeId != null && event.conversationId == activeId) {
                            _isReceiverTyping.value = event.isTyping
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun addMessageToList(message: Message) {
        val currentList = _messages.value.toMutableList()
        if (!currentList.any { it.id == message.id }) {
            currentList.add(0, message)
            _messages.value = currentList
        }
    }

    fun clearError() {
        _uiState.value = ChatUiState.Success
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
    }
}

sealed class ChatUiState {
    object Loading : ChatUiState()
    object LoadingMore : ChatUiState()
    object Success : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}
