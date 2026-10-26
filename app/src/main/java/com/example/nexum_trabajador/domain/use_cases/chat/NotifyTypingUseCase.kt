package com.example.nexum_trabajador.domain.use_cases.chat

import com.example.nexum_trabajador.domain.repository.MessagingRepository
import javax.inject.Inject

class NotifyTypingUseCase @Inject constructor(
    private val msgRepository : MessagingRepository
) {
    suspend operator fun invoke(conversationId : String, isTyping : Boolean) = msgRepository.notifyTyping(conversationId, isTyping)
}
