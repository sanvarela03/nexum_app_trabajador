package com.example.nexum_trabajador.domain.use_cases.sign_in

import com.example.nexum_trabajador.data.local_storage.AsyncStorage
import com.example.nexum_trabajador.security.TokenManager
import javax.inject.Inject

class GetLastUserEmailUseCase @Inject constructor(
    private val asyncStorage: AsyncStorage
) {
    suspend operator fun invoke(): String? {
        val lastUserEmail = asyncStorage.getItem("last_user_email")
        return lastUserEmail
    }
}
