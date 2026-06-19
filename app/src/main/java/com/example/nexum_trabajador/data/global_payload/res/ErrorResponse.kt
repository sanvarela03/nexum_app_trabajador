package com.example.nexum_trabajador.data.global_payload.res

data class ErrorResponse(
    val path: String,
    val error: String,
    val message: String,
    val status: Int
)
