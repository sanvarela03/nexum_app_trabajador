package com.example.nexum_trabajador.data.profile.remote.payload.res

import com.google.gson.annotations.SerializedName


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/9/2026
 * @version 1.0
 */
data class ProfileRes(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phone: String?,
    val imgUrl: String?,
    val dateJoined: String,
    val lastLogin: String,
    @SerializedName("enabled")
    val isEnabled: Boolean,
)
