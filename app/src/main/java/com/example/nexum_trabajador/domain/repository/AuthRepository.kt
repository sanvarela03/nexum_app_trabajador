package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.auth.remote.payload.req.SignInReq
import com.example.nexum_trabajador.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_trabajador.data.auth.remote.payload.res.SignInRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signIn(signInReq: SignInReq): Flow<ApiResponse<SignInRes>>
    fun signUp(signUpReq: SignUpReq): Flow<ApiResponse<MessageRes>>
    fun authenticate(): Flow<Boolean> // Devolvemos un Flow<Boolean> directamente
    fun signOut(): Flow<ApiResponse<MessageRes>>
}
