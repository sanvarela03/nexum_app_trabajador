package com.example.nexum_trabajador.domain.use_cases.chat

import com.example.nexum_trabajador.di.modules.WebSocketUrl
import com.example.nexum_trabajador.domain.repository.MessagingRepository
import com.example.nexum_trabajador.security.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ConnectWebSocketUseCase @Inject constructor(
    private val msgRepository : MessagingRepository,
    private val tokenManager : TokenManager,
    @WebSocketUrl private val wsUrl: String
) {
    suspend operator fun invoke() = tokenManager.getAccessToken().firstOrNull()?.let { token ->
        msgRepository.connectWebSocket(wsUrl, token)
    }
}
