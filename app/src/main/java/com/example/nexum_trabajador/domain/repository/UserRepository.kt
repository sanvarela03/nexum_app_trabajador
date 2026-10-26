package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun updateFcmToken(fcmToken: String): Flow<ApiResponse<Unit>>
}
