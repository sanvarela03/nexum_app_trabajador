package com.example.nexum_trabajador.data.job_offer.remote

import com.example.nexum_trabajador.data.job_offer.remote.payload.res.JobOfferWorkerDetailsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface JobOfferApi {

    @GET("/api/v1/job-offers/{jobOfferUuid}/worker-details")
    suspend fun getJobOfferWorkerDetails(
        @Path("jobOfferUuid") jobOfferUuid: String
    ): Response<JobOfferWorkerDetailsDto>

}
