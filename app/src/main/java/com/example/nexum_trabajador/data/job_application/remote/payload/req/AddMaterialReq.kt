package com.example.nexum_trabajador.data.job_application.remote.payload.req

data class AddMaterialReq(
    val name: String,
    val description: String,
    val type: String,
    val quantity: Int,
    val unitPrice: Double
)
