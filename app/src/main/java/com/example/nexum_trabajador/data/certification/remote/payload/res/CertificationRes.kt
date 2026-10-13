package com.example.nexum_trabajador.data.certification.remote.payload.res

import com.google.gson.annotations.SerializedName

data class CertificationRes(
    @SerializedName("id")
    val id: Long,

    @SerializedName("job_occupation_id")
    val jobOccupationId: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("certification_url")
    val certificationUrl: String?,

    @SerializedName("issued_by")
    val issuedBy: String? = null,

    @SerializedName("issued_date")
    val issuedDate: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)
