package com.example.nexum_trabajador.data.job_occupation.remote.payload.res

data class JobOccupationRes(
    val id: Long,
    val workerId: Long,
    val categoryId: Long,
    val categoryName: String?,
    val categoryDescription: String?,
    val categoryImageUrl: String?,
    val name: String,
    val description: String?,
    val yearsExperience: Int?,
    val isActive: Boolean?
)
