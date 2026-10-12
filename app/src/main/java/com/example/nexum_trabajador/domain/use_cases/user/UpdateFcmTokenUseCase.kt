package com.example.nexum_trabajador.domain.use_cases.user

import com.example.nexum_trabajador.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(token: String) = userRepository.updateFcmToken(token)
}
