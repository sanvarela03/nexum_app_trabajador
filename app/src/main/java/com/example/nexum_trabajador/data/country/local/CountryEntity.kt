package com.example.nexum_trabajador.data.country.local

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val id: Long,
    val name: String?,
    val code: String?,
    val phoneCode: String?,
    val flagEmoji: String?,
    val phoneCheckRegex: String?
)