package com.example.nexum_trabajador.data.user

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.user.remote.UserApi
import com.example.nexum_trabajador.data.user.remote.payload.req.UpdateFcmTokenReq
import com.example.nexum_trabajador.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override fun updateFcmToken(fcmToken: String): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {
            userApi.updateFcmToken(UpdateFcmTokenReq(fcmToken = fcmToken))
        }.map { response ->
            when (response) {
                is ApiResponse.Success -> ApiResponse.Success(Unit)
                is ApiResponse.Error -> ApiResponse.Error(response.errorMessage)
                is ApiResponse.Failure -> ApiResponse.Failure(response.errorMessage, response.code)
                ApiResponse.Loading -> ApiResponse.Loading
            }
        }
    }
}
