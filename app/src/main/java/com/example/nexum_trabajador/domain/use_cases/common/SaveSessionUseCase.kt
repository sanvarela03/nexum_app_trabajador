package com.example.nexum_trabajador.domain.use_cases.common

import com.example.nexum_trabajador.data.auth.remote.payload.res.SignInRes
import com.example.nexum_trabajador.security.TokenManager
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(response: SignInRes) {
        tokenManager.saveAccessToken(response.token)
        tokenManager.saveRefreshToken(response.refreshToken)
        tokenManager.saveUserId(response.userId)
    }
}
