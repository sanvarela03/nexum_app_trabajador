package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.job_offer.remote.payload.res.JobOfferWorkerDetailsDto
import kotlinx.coroutines.flow.Flow

interface JobOfferRepository {
    fun getJobOfferWorkerDetails(jobOfferUuid: String): Flow<ApiResponse<JobOfferWorkerDetailsDto>>
}
