package com.example.nexum_trabajador.data.insurance.remote.payload.res

import com.google.gson.annotations.SerializedName

data class InsuranceRes(
    @SerializedName("id")
    val id: Long,

    @SerializedName("worker_id")
    val workerId: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("coverage_type")
    val coverageType: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("insurance_url")
    val insuranceUrl: String?,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)
