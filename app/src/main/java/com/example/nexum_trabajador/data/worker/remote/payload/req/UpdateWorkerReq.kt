package com.example.nexum_trabajador.data.worker.remote.payload.req


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/17/2026
 * @version 1.0
 */
data class UpdateWorkerReq(
    val profileVerified: Boolean,
    val bio: String,
    val latitude: Double,
    val longitude: Double,
)