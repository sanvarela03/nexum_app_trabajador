package com.example.nexum_trabajador.data.country.remote.payload.res


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
data class CountryRes(
    val id: Long,
    val name: String,
    val code: String,
    val phoneCode: String,
    val flagEmoji: String,
    val phoneCheckRegex: String
)