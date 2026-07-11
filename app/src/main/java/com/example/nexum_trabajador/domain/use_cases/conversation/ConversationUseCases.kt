package com.example.nexum_trabajador.domain.use_cases.conversation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationUseCases @Inject constructor(
    val getConversations: GetConversationsUseCase,
    val getUnreadCount: GetUnreadCountUseCase,
    val observeIncomingMessages: ObserveIncomingMessagesUseCase
)
