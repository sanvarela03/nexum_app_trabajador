package com.example.nexum_trabajador.data.certification.repository

import com.example.nexum_trabajador.data.certification.remote.CertificationRemoteDataSource
import com.example.nexum_trabajador.data.certification.remote.payload.req.CreateCertificationReq
import com.example.nexum_trabajador.data.certification.remote.payload.res.CertificationRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.repository.CertificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CertificationRepositoryImpl @Inject constructor(
    private val remoteDataSource: CertificationRemoteDataSource
) : CertificationRepository {

    override fun getCertificationsByOccupation(occupationId: Long): Flow<ApiResponse<List<CertificationRes>>> =
        remoteDataSource.getCertificationsByOccupation(occupationId)

    override fun createCertification(req: CreateCertificationReq): Flow<ApiResponse<CertificationRes>> =
        remoteDataSource.createCertification(req)

    override fun deleteCertification(id: Long): Flow<ApiResponse<Unit>> =
        remoteDataSource.deleteCertification(id)
}
