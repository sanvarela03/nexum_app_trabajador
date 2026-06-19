package com.example.nexum_trabajador.domain.use_cases.common

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetFcmTokenUseCase @Inject constructor() {
    suspend operator fun invoke(): Result<String> {
        return try {
            val token = FirebaseMessaging.getInstance().token.await()
            Result.success(token)
        } catch (e: Exception) {
            Log.e("GetFcmTokenUseCase", "Error getting FCM token", e)
            Result.failure(e)
        }
    }
}
