package com.example.nexum_trabajador.data.insurance.remote.payload.req

import com.google.gson.annotations.SerializedName

data class CreateInsuranceReq(
    @SerializedName("name")
    val name: String,

    @SerializedName("coverage_type")
    val coverageType: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("insurance_url")
    val insuranceUrl: String? = null
)
