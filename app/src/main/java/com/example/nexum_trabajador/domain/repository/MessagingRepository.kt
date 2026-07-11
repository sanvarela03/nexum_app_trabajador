package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.message.remote.payload.req.MessageRequest
import com.example.nexum_trabajador.data.message.remote.websocket.WebSocketEvent
import com.example.nexum_trabajador.domain.model.ConnectionState
import com.example.nexum_trabajador.domain.model.Conversation
import com.example.nexum_trabajador.domain.model.Message
import com.example.nexum_trabajador.domain.model.PageResponse
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.0
 */
interface MessagingRepository {
    val connectionState: SharedFlow<ConnectionState>
    val incomingMessages: SharedFlow<Message?>
    val webSocketEvents: SharedFlow<WebSocketEvent>

    suspend fun connectWebSocket(serverUrl: String, jwtToken: String)
    suspend fun disconnectWebSocket()
    suspend fun subscribeToMessages()
    suspend fun sendMessageViaWebSocket(message: MessageRequest)
    suspend fun sendMessageViaRest(message: MessageRequest): Result<Message>
    suspend fun markAsReadViaRest(conversationId: String): Result<Unit>
    suspend fun markAsReadViaWebSocket(conversationId: String)
    suspend fun notifyTyping(conversationId: String, isTyping: Boolean)
    suspend fun getConversations(page: Int, size: Int): Result<PageResponse<Conversation>>
    suspend fun getConversationMessages(
        conversationId: String,
        page: Int,
        size: Int
    ): Result<PageResponse<Message>>

    suspend fun getUnreadCount(): Result<Long>
}