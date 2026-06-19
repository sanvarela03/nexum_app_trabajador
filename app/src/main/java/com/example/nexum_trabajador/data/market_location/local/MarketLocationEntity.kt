package com.example.nexum_trabajador.data.market_location.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 04/09/2025
 * @version 1.0
 */
@Entity(tableName = "MarketLocationEntity")
data class MarketLocationEntity(
    @PrimaryKey
    val id: Long,
    val city: String,
    val state: String,
    val country: String,
    val countryCode: String
)
