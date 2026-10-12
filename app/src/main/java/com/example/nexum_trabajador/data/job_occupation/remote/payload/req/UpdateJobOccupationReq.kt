package com.example.nexum_trabajador.data.job_occupation.remote.payload.req

data class UpdateJobOccupationReq(
    val name: String? = null,
    val description: String? = null,
    val yearsExperience: Int? = null,
    val isActive: Boolean? = null
)
