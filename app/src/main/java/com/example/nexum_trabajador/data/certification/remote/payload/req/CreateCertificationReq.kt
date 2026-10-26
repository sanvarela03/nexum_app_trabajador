package com.example.nexum_trabajador.data.certification.remote.payload.req

import com.google.gson.annotations.SerializedName

data class CreateCertificationReq(
    @SerializedName("job_occupation_id")
    val jobOccupationId: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("certification_url")
    val certificationUrl: String,

    @SerializedName("issued_by")
    val issuedBy: String? = null,

    @SerializedName("issued_date")
    val issuedDate: String? = null
)
