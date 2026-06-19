package com.example.nexum_trabajador.data.job_application.remote

import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddJobApplicationReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface JobApplicationApi {

    @POST("/api/v1/job-applications")
    suspend fun addJobApplication(
        @Body req: AddJobApplicationReq
    ): Response<Unit>

}
