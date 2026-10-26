package com.example.nexum_trabajador.data.job_occupation.remote.payload.req

import com.google.gson.annotations.SerializedName

data class CreateJobOccupationReq(
    @SerializedName("category_id")
    val categoryId: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("years_experience")
    val yearsExperience: Int? = null
)
