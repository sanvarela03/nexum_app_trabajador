package com.example.nexum_trabajador.data.market_location.remote.payload.res

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 04/09/2025
 * @version 1.0
 */
data class MarketLocationRes (
    val id: Long,
    val city: String,
    val state: String,
    val country: String,
    val countryCode: String,
)