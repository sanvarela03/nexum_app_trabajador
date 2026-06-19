package com.example.nexum_trabajador.data.country.mapper

import com.example.nexum_trabajador.data.country.local.CountryEntity
import com.example.nexum_trabajador.data.country.remote.payload.res.CountryRes
import com.example.nexum_trabajador.data.mapper.DomainMapper
import com.example.nexum_trabajador.data.mapper.EntityMapper
import com.example.nexum_trabajador.domain.model.Country

object CountryMapper: EntityMapper<CountryRes, CountryEntity>, DomainMapper<CountryEntity, Country> {

    // De Red (DTO) a Base de Datos (Entity)
    override fun toEntity(dto: CountryRes): CountryEntity {
        return CountryEntity(
            id = dto.id,
            name = dto.name,
            code = dto.code,
            phoneCode = dto.phoneCode,
            flagEmoji = dto.flagEmoji,
            phoneCheckRegex = dto.phoneCheckRegex
        )
    }

    // De Base de Datos (Entity) a Dominio (Modelo para la UI)
    override fun toDomain(entity: CountryEntity): Country {
        return Country(
            id = entity.id,
            name = entity.name,
            code = entity.code,
            phoneCode = entity.phoneCode,
            flagEmoji = entity.flagEmoji,
            phoneCheckRegex = entity.phoneCheckRegex
        )
    }
}
