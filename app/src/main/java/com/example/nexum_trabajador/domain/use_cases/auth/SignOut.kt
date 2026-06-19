package com.example.nexum_trabajador.domain.use_cases.auth

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import com.example.nexum_trabajador.domain.repository.AuthRepository
import com.example.nexum_trabajador.security.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class SignOut @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
) {
    operator fun invoke() : Flow<ApiResponse<MessageRes>> {
        return authRepository.signOut()
            .onCompletion {
                tokenManager.deleteAccessToken()
                tokenManager.deleteRefreshToken()
            }
    }
}
