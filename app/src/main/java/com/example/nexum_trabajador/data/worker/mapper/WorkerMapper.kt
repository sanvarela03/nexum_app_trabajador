package com.example.nexum_trabajador.data.worker.mapper

import com.example.nexum_trabajador.data.mapper.DomainMapper
import com.example.nexum_trabajador.data.mapper.EntityMapper
import com.example.nexum_trabajador.data.worker.local.WorkerEntity
import com.example.nexum_trabajador.data.worker.remote.payload.req.UpdateWorkerReq
import com.example.nexum_trabajador.data.worker.remote.payload.res.WorkerRes
import com.example.nexum_trabajador.domain.model.Worker

object WorkerMapper : EntityMapper<WorkerRes, WorkerEntity>, DomainMapper<WorkerEntity, Worker> {
    override fun toEntity(dto: WorkerRes): WorkerEntity {
        val workerEntity = WorkerEntity(
            workerId = dto.workerId,
            userId = dto.user.userId,
            name = dto.user.firstName,
            lastName = dto.user.lastName,
            email = dto.user.email,
            phone = dto.user.phone,
            profilePictureUrl = dto.user.imgUrl,
            isEnabled = dto.user.enabled,
            dateJoined = dto.user.dateJoined,
            lastLogin = dto.user.lastLogin,
            verificationCode = dto.user.verificationCode,
            verificationCodeTimestamp = dto.user.verificationCodeTimestamp,
            firebaseToken = dto.user.firebaseToken,
            username = dto.user.username,
            profileVerified = dto.profileVerified,
            latitude = dto.latitude,
            longitude = dto.longitude,
            bio = dto.workerBio,
            isAvailable = dto.isAvailable ?: true
        )
        return workerEntity
    }

    override fun toDomain(entity: WorkerEntity): Worker {
        return Worker(
            id = entity.id,
            firstName = entity.name ?: "",
            profilePictureUrl = entity.profilePictureUrl ?: "",
            lastName = entity.lastName ?: "",
            email = entity.email ?: "",
            username = entity.username,
            phone = entity.phone ?: "",
            isEnabled = entity.isEnabled,
            dateJoined = entity.dateJoined,
            lastLogin = entity.lastLogin,
            profileVerified = entity.profileVerified,
            verificationCode = entity.verificationCode,
            verificationCodeTimestamp = entity.verificationCodeTimestamp,
            firebaseToken = entity.firebaseToken,
            latitude = entity.latitude,
            longitude = entity.longitude,
            bio = entity.bio ?: "",
            isAvailable = entity.isAvailable
        )
    }

    fun toUpdateReq(worker: Worker): UpdateWorkerReq {
        return UpdateWorkerReq(
            profileVerified = worker.profileVerified,
            bio = worker.bio,
            latitude = worker.latitude ?: 0.0,
            longitude = worker.longitude ?: 0.0,
            isAvailable = worker.isAvailable
        )
    }
}