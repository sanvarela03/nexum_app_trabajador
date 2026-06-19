package com.example.nexum_trabajador.data.worker.local

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/18/2025
 * @version 1.0
 */
@Entity(tableName = "workers")
data class WorkerEntity(
    @PrimaryKey
    val id: Long = 1L,
    val workerId: Long?,
    val userId: Long?,
    val name: String?,
    val lastName: String?,
    val username: String,
    val email: String?,
    val bio: String?,
    val verificationCode: String?,
    val verificationCodeTimestamp: String?,
    val firebaseToken: String?,
    val phone: String?,
    val isEnabled: Boolean,
    val profileVerified: Boolean,
    val dateJoined: String,
    val lastLogin: String,
    val profilePictureUrl: String?,
    val latitude: Double?,
    val longitude: Double?
)
