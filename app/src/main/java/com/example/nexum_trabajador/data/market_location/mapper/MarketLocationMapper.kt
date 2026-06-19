package com.example.nexum_trabajador.data.market_location.mapper

import com.example.nexum_trabajador.data.mapper.DomainMapper
import com.example.nexum_trabajador.data.mapper.EntityMapper
import com.example.nexum_trabajador.data.market_location.local.MarketLocationEntity
import com.example.nexum_trabajador.data.market_location.remote.payload.res.MarketLocationRes
import com.example.nexum_trabajador.domain.model.MarketLocation

object MarketLocationMapper : EntityMapper<MarketLocationRes, MarketLocationEntity>,
    DomainMapper<MarketLocationEntity, MarketLocation> {
    override fun toEntity(dto: MarketLocationRes): MarketLocationEntity {
        return MarketLocationEntity(
            id = dto.id,
            city = dto.city,
            state = dto.state,
            country = dto.country,
            countryCode = dto.countryCode
        )
    }

    override fun toDomain(entity: MarketLocationEntity): MarketLocation {
        return MarketLocation(
            id = entity.id,
            city = entity.city,
            state = entity.state,
            country = entity.country,
            countryCode = entity.countryCode
        )
    }
}