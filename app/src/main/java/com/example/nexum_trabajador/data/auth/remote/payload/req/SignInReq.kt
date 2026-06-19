package com.example.nexum_trabajador.data.auth.remote.payload.req

data class SignInReq(
    val username: String,
    val password: String,
    val firebaseToken: String
)
