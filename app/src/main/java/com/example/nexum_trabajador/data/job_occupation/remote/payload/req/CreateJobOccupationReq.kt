package com.example.nexum_trabajador.data.job_occupation.remote.payload.req

data class CreateJobOccupationReq(
    val categoryId: Long,
    val name: String,
    val description: String? = null,
    val yearsExperience: Int? = null
)
