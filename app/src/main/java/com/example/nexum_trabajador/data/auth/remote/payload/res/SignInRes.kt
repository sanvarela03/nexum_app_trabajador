package com.example.nexum_trabajador.data.auth.remote.payload.res

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRes(
    @SerialName("id")
    val userId: Long,
    val username: String,
    val email: String,
    val token: String?,
    val refreshToken: String?,
    val type: String,
    val roles: List<String>
)
