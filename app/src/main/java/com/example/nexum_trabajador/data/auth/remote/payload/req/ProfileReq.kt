package com.example.nexum_trabajador.data.auth.remote.payload.req

import com.google.gson.annotations.SerializedName

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 25/10/2025
 * @version 1.0
 */
data class ProfileReq(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("birth_date")
    val birthDate: String,
    @SerializedName("profile_image_url")
    val profileImageUrl: String,
    @SerializedName("front_document_url")
    val frontDocumentUrl: String,
    @SerializedName("back_document_url")
    val backDocumentUrl: String,
    @SerializedName("document_number")
    val documentNumber: String? = null,
    @SerializedName("document_type")
    val documentType: String? = "CC",
)