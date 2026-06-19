package com.example.nexum_trabajador.domain.model


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/11/2025
 * @version 1.0
 */
data class MarketLocation(
    val id: Long,
    val city: String,
    val state: String,
    val country: String,
    val countryCode: String
)