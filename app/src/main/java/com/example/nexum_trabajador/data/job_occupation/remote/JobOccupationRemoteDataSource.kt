package com.example.nexum_trabajador.data.job_occupation.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.CreateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.UpdateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobOccupationRemoteDataSource @Inject constructor(
    private val api: JobOccupationApi
) {
    fun getMyOccupations(): Flow<ApiResponse<List<JobOccupationRes>>> =
        apiRequestFlow { api.getMyOccupations() }

    fun createOccupation(req: CreateJobOccupationReq): Flow<ApiResponse<JobOccupationRes>> =
        apiRequestFlow { api.createOccupation(req) }

    fun updateOccupation(id: Long, req: UpdateJobOccupationReq): Flow<ApiResponse<JobOccupationRes>> =
        apiRequestFlow { api.updateOccupation(id, req) }

    fun updateStatus(id: Long, active: Boolean): Flow<ApiResponse<JobOccupationRes>> =
        apiRequestFlow { api.updateStatus(id, active) }

    fun deleteOccupation(id: Long): Flow<ApiResponse<Unit>> =
        apiRequestFlow { api.deleteOccupation(id) }
}
