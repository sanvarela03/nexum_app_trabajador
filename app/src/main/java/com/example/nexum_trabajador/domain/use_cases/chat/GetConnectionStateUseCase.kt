package com.example.nexum_trabajador.domain.use_cases.chat

import com.example.nexum_trabajador.domain.repository.MessagingRepository
import javax.inject.Inject

class GetConnectionStateUseCase @Inject constructor(
    private val msgRepository : MessagingRepository
) {
    operator fun invoke() = msgRepository.connectionState
}
