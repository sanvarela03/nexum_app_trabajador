package com.example.nexum_trabajador.domain.use_cases.conversation

import com.example.nexum_trabajador.domain.repository.MessagingRepository
import javax.inject.Inject

class GetUnreadCountUseCase @Inject constructor(
    private val repository: MessagingRepository
) {
    suspend operator fun invoke(): Result<Long> {
        return repository.getUnreadCount()
    }
}
