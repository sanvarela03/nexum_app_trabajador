package com.example.nexum_trabajador.data.profile.local

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/12/2026
 * @version 1.0
 */
@Entity
data class ProfileEntity(
    @PrimaryKey
    val id: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phone: String,
    val imgUrl: String,
    val dateJoined: String,
    val lastLogin: String,
    val isEnabled: Boolean,
)