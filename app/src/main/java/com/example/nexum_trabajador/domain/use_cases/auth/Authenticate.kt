package com.example.nexum_trabajador.domain.use_cases.auth

import com.example.nexum_trabajador.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Authenticate @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return authRepository.authenticate()
    }
}
