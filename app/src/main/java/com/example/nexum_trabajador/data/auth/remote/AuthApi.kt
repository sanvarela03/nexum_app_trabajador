package com.example.nexum_trabajador.data.auth.remote

import com.example.nexum_trabajador.data.auth.remote.payload.req.RefreshReq
import com.example.nexum_trabajador.data.auth.remote.payload.req.SignInReq
import com.example.nexum_trabajador.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_trabajador.data.auth.remote.payload.res.RefreshRes
import com.example.nexum_trabajador.data.auth.remote.payload.res.SignInRes
import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/sign-in")
    suspend fun signIn(@Body signInReq: SignInReq): Response<SignInRes>
    @POST("/api/v1/auth/sign-up")
    suspend fun signUp(@Body signUpReq: SignUpReq): Response<MessageRes>
    @POST("/api/v1/auth/sign-out")
    suspend fun signOut(): Response<MessageRes>
    @POST("/api/v1/auth/refresh-token")
    suspend fun refreshToken(@Body refreshRequest: RefreshReq): Response<RefreshRes>
}