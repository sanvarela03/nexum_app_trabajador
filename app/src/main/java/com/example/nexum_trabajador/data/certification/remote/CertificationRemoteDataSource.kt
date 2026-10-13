package com.example.nexum_trabajador.data.certification.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.certification.remote.payload.req.CreateCertificationReq
import com.example.nexum_trabajador.data.certification.remote.payload.res.CertificationRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CertificationRemoteDataSource @Inject constructor(
    private val api: CertificationApi
) {
    fun getCertificationsByOccupation(occupationId: Long): Flow<ApiResponse<List<CertificationRes>>> =
        apiRequestFlow { api.getCertificationsByOccupation(occupationId) }

    fun createCertification(req: CreateCertificationReq): Flow<ApiResponse<CertificationRes>> =
        apiRequestFlow { api.createCertification(req) }

    fun deleteCertification(id: Long): Flow<ApiResponse<Unit>> =
        apiRequestFlow { api.deleteCertification(id) }
}
