package com.example.nexum_trabajador.data.insurance.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.insurance.remote.InsuranceRemoteDataSource
import com.example.nexum_trabajador.data.insurance.remote.payload.req.CreateInsuranceReq
import com.example.nexum_trabajador.data.insurance.remote.payload.res.InsuranceRes
import com.example.nexum_trabajador.domain.repository.InsuranceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsuranceRepositoryImpl @Inject constructor(
    private val remoteDataSource: InsuranceRemoteDataSource
) : InsuranceRepository {

    override fun getMyInsurances(): Flow<ApiResponse<List<InsuranceRes>>> =
        remoteDataSource.getMyInsurances()

    override fun createInsurance(req: CreateInsuranceReq): Flow<ApiResponse<InsuranceRes>> =
        remoteDataSource.createInsurance(req)

    override fun deleteInsurance(id: Long): Flow<ApiResponse<Unit>> =
        remoteDataSource.deleteInsurance(id)
}
