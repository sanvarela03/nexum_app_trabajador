package com.example.nexum_trabajador.data.auth

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.auth.remote.AuthApi
import com.example.nexum_trabajador.data.auth.remote.payload.req.SignInReq
import com.example.nexum_trabajador.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_trabajador.data.auth.remote.payload.res.SignInRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import com.example.nexum_trabajador.di.modules.IoDispatcher
import com.example.nexum_trabajador.domain.repository.AuthRepository
import com.example.nexum_trabajador.security.TokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AuthRepository {

    override fun signIn(signInReq: SignInReq): Flow<ApiResponse<SignInRes>> {
        return apiRequestFlow { authApi.signIn(signInReq) }
    }

    override fun signUp(signUpReq: SignUpReq): Flow<ApiResponse<MessageRes>> {
        return apiRequestFlow { authApi.signUp(signUpReq) }
    }

    override fun authenticate(): Flow<Boolean> {
        return tokenManager.getAccessToken()
            .map { it != null }
    }

    override fun signOut(): Flow<ApiResponse<MessageRes>> {
        // Delegamos la gestión de tokens al Use Case (SignOut.kt) usando onCompletion.
        // Aquí solo nos encargamos de realizar la petición al servidor.
        return apiRequestFlow { authApi.signOut() }
    }
}
