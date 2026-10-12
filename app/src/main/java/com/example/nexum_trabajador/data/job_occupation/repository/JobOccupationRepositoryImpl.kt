package com.example.nexum_trabajador.data.job_occupation.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.job_occupation.remote.JobOccupationRemoteDataSource
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.CreateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.UpdateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import com.example.nexum_trabajador.domain.repository.JobOccupationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobOccupationRepositoryImpl @Inject constructor(
    private val remoteDataSource: JobOccupationRemoteDataSource
) : JobOccupationRepository {
    override fun getMyOccupations(): Flow<ApiResponse<List<JobOccupationRes>>> =
        remoteDataSource.getMyOccupations()

    override fun createOccupation(req: CreateJobOccupationReq): Flow<ApiResponse<JobOccupationRes>> =
        remoteDataSource.createOccupation(req)

    override fun updateOccupation(id: Long, req: UpdateJobOccupationReq): Flow<ApiResponse<JobOccupationRes>> =
        remoteDataSource.updateOccupation(id, req)

    override fun updateStatus(id: Long, active: Boolean): Flow<ApiResponse<JobOccupationRes>> =
        remoteDataSource.updateStatus(id, active)

    override fun deleteOccupation(id: Long): Flow<ApiResponse<Unit>> =
        remoteDataSource.deleteOccupation(id)
}
