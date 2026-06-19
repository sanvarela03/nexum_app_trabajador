package com.example.nexum_trabajador.domain.use_cases.sign_in

import com.example.nexum_trabajador.data.local_storage.AsyncStorage
import javax.inject.Inject

class SaveLastUserEmailUseCase @Inject constructor(
    private val asyncStorage: AsyncStorage
) {
    suspend operator fun invoke(email: String) {
        asyncStorage.setItem("last_user_email", email)
    }
}
