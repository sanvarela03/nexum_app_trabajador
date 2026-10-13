package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.insurance.remote.payload.req.CreateInsuranceReq
import com.example.nexum_trabajador.data.insurance.remote.payload.res.InsuranceRes
import kotlinx.coroutines.flow.Flow

interface InsuranceRepository {
    fun getMyInsurances(): Flow<ApiResponse<List<InsuranceRes>>>
    fun createInsurance(req: CreateInsuranceReq): Flow<ApiResponse<InsuranceRes>>
    fun deleteInsurance(id: Long): Flow<ApiResponse<Unit>>
}
