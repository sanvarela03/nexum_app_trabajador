package com.example.nexum_trabajador.data.job_occupation.remote.payload.req

import com.google.gson.annotations.SerializedName

data class UpdateJobOccupationReq(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("years_experience")
    val yearsExperience: Int? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null
)
