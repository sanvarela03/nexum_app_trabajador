package com.example.nexum_trabajador.domain.model

data class Worker(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String,
    val email: String,
    val bio: String,
    val username: String,
    val phone: String,
    val isEnabled: Boolean,
    val profileVerified: Boolean,
    val verificationCode: String?,
    val verificationCodeTimestamp: String?,
    val firebaseToken: String?,
    val dateJoined: String,
    val lastLogin: String,
    val latitude: Double?,
    val longitude: Double?,
    val address: String = "Sin dirección",
    val isAvailable: Boolean = true
)
