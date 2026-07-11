package com.example.nexum_trabajador.domain.model

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.0
 */
@Serializable
data class Conversation(
    val id: String,
    val participantIds: Set<String>,
    val participantRoles: Set<String>,
    val jobOfferId: String? = null,
    val lastMessageContent: String? = null,
    val lastMessageAt: String? = null,
    val createdAt: String,
    val unreadCount: Int = 0,
    val isActive: Boolean = true,
    val metadata: Map<String, String>? = null,
    val lastMessage: Message? = null
)

@Serializable
data class PageResponse<T>(
    val content: List<T> = emptyList(),
    val totalPages: Int = 0,
    val totalElements: Long = 0L,
    val number: Int = 0,
    val size: Int = 0
)

@Serializable
data class UnreadCountResponse(
    val unreadCount: Long
)
