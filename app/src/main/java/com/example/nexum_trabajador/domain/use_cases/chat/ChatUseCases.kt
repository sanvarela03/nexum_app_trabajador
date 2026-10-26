package com.example.nexum_trabajador.domain.use_cases.chat

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatUseCases @Inject constructor(
    val connectWebSocket: ConnectWebSocketUseCase,
    val disconnectWebSocket: DisconnectWebSocketUseCase,
    val loadConversationMessages : LoadConversationMessagesUseCase,
    val getConnectionState : GetConnectionStateUseCase,
    val sendMessage: SendMessageUseCase,
    val markMessageAsRead: MarkMessageAsReadUseCase,
    val notifyTyping: NotifyTypingUseCase,
    val getIncomingMessages: GetIncomingMessagesUseCase,
    val getWebSocketEvents : GetWebsocketEventsUseCase
)
