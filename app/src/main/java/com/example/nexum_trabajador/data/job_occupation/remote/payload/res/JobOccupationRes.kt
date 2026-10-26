package com.example.nexum_trabajador.data.job_occupation.remote.payload.res

import com.google.gson.annotations.SerializedName

data class JobOccupationRes(
    @SerializedName("id")
    val id: Long,

    @SerializedName("worker_id")
    val workerId: Long,

    @SerializedName("category_id")
    val categoryId: Long,

    @SerializedName("category_name")
    val categoryName: String?,

    @SerializedName("category_description")
    val categoryDescription: String? = null,

    @SerializedName("category_image_url")
    val categoryImageUrl: String? = null,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("years_experience")
    val yearsExperience: Int?,

    @SerializedName("is_active")
    val isActive: Boolean?,

    @SerializedName("certifications")
    val certifications: List<com.example.nexum_trabajador.data.certification.remote.payload.res.CertificationRes> = emptyList(),

    @SerializedName("skills")
    val skills: List<com.example.nexum_trabajador.data.skill_set.remote.payload.res.SkillSetRes> = emptyList()
)
