package com.example.nexum_trabajador.data.message.remote.payload.req

import com.example.nexum_trabajador.domain.model.MessageType
import kotlinx.serialization.Serializable

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.0
 */
@Serializable
data class MessageRequest(
    val receiverId: String,
    val receiverRole: String,
    val type: MessageType,
    val content: String,
    val replyToMessageId: String? = null,
    val metadata: Map<String, String>? = null,
    val jobOfferId: String? = null
)
