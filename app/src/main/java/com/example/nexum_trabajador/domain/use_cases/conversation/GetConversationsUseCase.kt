package com.example.nexum_trabajador.domain.use_cases.conversation

import com.example.nexum_trabajador.domain.model.Conversation
import com.example.nexum_trabajador.domain.model.PageResponse
import com.example.nexum_trabajador.domain.repository.MessagingRepository
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor(
    private val repository: MessagingRepository
) {
    suspend operator fun invoke(page: Int, size: Int): Result<PageResponse<Conversation>> {
        return repository.getConversations(page, size)
    }
}
