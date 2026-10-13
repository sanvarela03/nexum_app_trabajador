package com.example.nexum_trabajador.data.insurance.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.insurance.remote.payload.req.CreateInsuranceReq
import com.example.nexum_trabajador.data.insurance.remote.payload.res.InsuranceRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsuranceRemoteDataSource @Inject constructor(
    private val api: InsuranceApi
) {
    fun getMyInsurances(): Flow<ApiResponse<List<InsuranceRes>>> =
        apiRequestFlow { api.getMyInsurances() }

    fun createInsurance(req: CreateInsuranceReq): Flow<ApiResponse<InsuranceRes>> =
        apiRequestFlow { api.createInsurance(req) }

    fun deleteInsurance(id: Long): Flow<ApiResponse<Unit>> =
        apiRequestFlow { api.deleteInsurance(id) }
}
