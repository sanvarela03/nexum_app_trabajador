package com.example.nexum_trabajador.data.user.remote.payload.res

import kotlinx.serialization.Serializable

@Serializable
data class UserRes(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val verificationCode: String? = null,
    val verificationCodeTimestamp: String? = null,
    val firebaseToken: String? = null,
    val phone: String,
    val dateJoined: String,
    val lastLogin: String,
    val imgUrl: String? = null,
    val roles: List<String>,
    val enabled: Boolean
)
