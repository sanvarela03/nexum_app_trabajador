package com.example.nexum_trabajador.data.profile.mapper

import android.util.Log
import com.example.nexum_trabajador.data.mapper.DomainMapper
import com.example.nexum_trabajador.data.mapper.EntityMapper
import com.example.nexum_trabajador.data.profile.local.ProfileEntity
import com.example.nexum_trabajador.data.profile.remote.payload.res.ProfileRes
import com.example.nexum_trabajador.domain.model.Profile

object ProfileMapper : EntityMapper<ProfileRes, ProfileEntity>, DomainMapper<ProfileEntity, Profile> {
    override fun toEntity(dto: ProfileRes): ProfileEntity {
        Log.d("ProfileMapper", "Mapping DTO to Entity - ID: ${dto.id}, imgUrl: ${dto.imgUrl}")
        return ProfileEntity(
            id = dto.id,
            firstName = dto.firstName,
            lastName = dto.lastName,
            username = dto.username,
            email = dto.email,
            phone = dto.phone ?: "NaN",
            imgUrl = dto.imgUrl ?: "NaN",
            dateJoined = dto.dateJoined,
            lastLogin = dto.lastLogin,
            isEnabled = dto.isEnabled,
        )
    }

    override fun toDomain(entity: ProfileEntity): Profile {
        Log.d("ProfileMapper", "Mapping Entity to Domain - ID: ${entity.id}, imgUrl: ${entity.imgUrl}")
        return Profile(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            username = entity.username,
            email = entity.email,
            phone = entity.phone,
            imgUrl = entity.imgUrl,
            dateJoined = entity.dateJoined,
            lastLogin = entity.lastLogin,
            isEnabled = entity.isEnabled,
        )
    }
}
