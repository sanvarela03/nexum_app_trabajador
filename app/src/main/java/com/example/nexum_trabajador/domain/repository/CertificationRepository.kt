package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.certification.remote.payload.req.CreateCertificationReq
import com.example.nexum_trabajador.data.certification.remote.payload.res.CertificationRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

interface CertificationRepository {
    fun getCertificationsByOccupation(occupationId: Long): Flow<ApiResponse<List<CertificationRes>>>
    fun createCertification(req: CreateCertificationReq): Flow<ApiResponse<CertificationRes>>
    fun deleteCertification(id: Long): Flow<ApiResponse<Unit>>
}
