package com.example.nexum_trabajador.domain.use_cases.chat

import com.example.nexum_trabajador.domain.model.Message
import com.example.nexum_trabajador.domain.model.PageResponse
import com.example.nexum_trabajador.domain.repository.MessagingRepository
import javax.inject.Inject

class LoadConversationMessagesUseCase @Inject constructor (
    private val msgRepository: MessagingRepository
) {
    suspend operator fun invoke(conversationId: String, currentPage: Int, pageSize: Int): Result<PageResponse<Message>> {
        return msgRepository.getConversationMessages(conversationId, currentPage, pageSize)
    }
}
