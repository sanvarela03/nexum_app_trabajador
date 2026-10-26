package com.example.nexum_trabajador.data.user.remote

import com.example.nexum_trabajador.data.user.remote.payload.req.UpdateFcmTokenReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface UserApi {
    @PUT("/api/v1/users/fcm-token")
    suspend fun updateFcmToken(
        @Body req: UpdateFcmTokenReq
    ): Response<Map<String, String>>
}
