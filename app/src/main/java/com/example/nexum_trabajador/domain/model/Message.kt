package com.example.nexum_trabajador.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.0
 */
@Serializable
enum class MessageType {
    TEXT, IMAGE, FILE, LOCATION, AUDIO, SYSTEM
}

@Serializable
enum class MessageStatus {
    SENT, DELIVERED, READ
}

@Serializable
data class Message(
    val id: String = "",
    val conversationId: String,
    val senderId: String,
    val senderRole: String,
    val receiverId: String,
    val receiverRole: String,
    val type: MessageType,
    val content: String,
    val status: MessageStatus = MessageStatus.SENT,
    val timestamp: String,
    val deliveredAt: String? = null,
    val readAt: String? = null,
    val metadata: Map<String, JsonElement>? = null,
    val replyToMessageId: String? = null
)
