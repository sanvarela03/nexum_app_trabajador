package com.example.nexum_trabajador.domain.use_cases.chat

import com.example.nexum_trabajador.domain.repository.MessagingRepository
import javax.inject.Inject

class MarkMessageAsReadUseCase @Inject constructor(
    private val msgRepository : MessagingRepository
) {
    suspend operator fun invoke(id : String) = msgRepository.markAsReadViaWebSocket(id)
}
