package com.example.nexum_trabajador.data.message.remote.api

import com.example.nexum_trabajador.data.message.remote.payload.req.MessageRequest
import com.example.nexum_trabajador.di.modules.BaseUrl
import com.example.nexum_trabajador.domain.model.Conversation
import com.example.nexum_trabajador.domain.model.Message
import com.example.nexum_trabajador.domain.model.PageResponse
import com.example.nexum_trabajador.domain.model.UnreadCountResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.0
 */
@Singleton
class MessagingApiService @Inject constructor(
    private val httpClient: HttpClient,
    @BaseUrl private val baseUrl: String
) {

    suspend fun sendMessage(
        token: String,
        message: MessageRequest
    ): Message {
        return httpClient.post("$baseUrl/api/v1/messages") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(message)
        }.body()
    }

    suspend fun getConversations(
        token: String,
        page: Int,
        size: Int
    ): PageResponse<Conversation> {
        return httpClient.get("$baseUrl/api/v1/messages/conversations") {
            bearerAuth(token)
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getConversationMessages(
        token: String,
        conversationId: String,
        page: Int,
        size: Int
    ): PageResponse<Message> {
        return httpClient.get("$baseUrl/api/v1/messages/conversations/$conversationId") {
            bearerAuth(token)
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun markAsRead(
        token: String,
        conversationId: String
    ) {
        httpClient.put("$baseUrl/api/v1/messages/conversations/$conversationId/read") {
            bearerAuth(token)
        }
    }

    suspend fun getUnreadCount(token: String): UnreadCountResponse {
        return httpClient.get("$baseUrl/api/v1/messages/unread/count") {
            bearerAuth(token)
        }.body()
    }
}