package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.CreateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.UpdateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import kotlinx.coroutines.flow.Flow

interface JobOccupationRepository {
    fun getMyOccupations(): Flow<ApiResponse<List<JobOccupationRes>>>
    fun createOccupation(req: CreateJobOccupationReq): Flow<ApiResponse<JobOccupationRes>>
    fun updateOccupation(id: Long, req: UpdateJobOccupationReq): Flow<ApiResponse<JobOccupationRes>>
    fun updateStatus(id: Long, active: Boolean): Flow<ApiResponse<JobOccupationRes>>
    fun deleteOccupation(id: Long): Flow<ApiResponse<Unit>>
}
