package com.example.nexum_trabajador.data.worker.remote.payload.res

import com.example.nexum_trabajador.data.user.remote.payload.res.UserRes
import kotlinx.serialization.Serializable

@Serializable
data class WorkerRes(
    val workerId: Long,
    val profileVerified: Boolean,
    val documentUrl: String? = null,
    val workerBio: String,
    val user: UserRes,
    val latitude: Double? = null,
    val longitude: Double? = null
)
