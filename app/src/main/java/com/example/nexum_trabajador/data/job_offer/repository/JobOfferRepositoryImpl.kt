package com.example.nexum_trabajador.data.job_offer.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.job_offer.remote.JobOfferApi
import com.example.nexum_trabajador.data.job_offer.remote.payload.res.JobOfferWorkerDetailsDto
import com.example.nexum_trabajador.domain.repository.JobOfferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JobOfferRepositoryImpl @Inject constructor(
    private val api: JobOfferApi
) : JobOfferRepository {

    override fun getJobOfferWorkerDetails(jobOfferUuid: String): Flow<ApiResponse<JobOfferWorkerDetailsDto>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = api.getJobOfferWorkerDetails(jobOfferUuid)
            if (response.isSuccessful && response.body() != null) {
                emit(ApiResponse.Success(response.body()!!))
            } else {
                emit(ApiResponse.Error(response.message() ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message ?: "Error de conexión"))
        }
    }
}
