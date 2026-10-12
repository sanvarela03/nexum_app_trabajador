package com.example.nexum_trabajador.data.job_occupation.remote

import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.CreateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.UpdateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface JobOccupationApi {
    @GET("/api/v1/job-occupations/my-occupations")
    suspend fun getMyOccupations(): Response<List<JobOccupationRes>>

    @POST("/api/v1/job-occupations")
    suspend fun createOccupation(@Body req: CreateJobOccupationReq): Response<JobOccupationRes>

    @PUT("/api/v1/job-occupations/{id}")
    suspend fun updateOccupation(
        @Path("id") id: Long,
        @Body req: UpdateJobOccupationReq
    ): Response<JobOccupationRes>

    @PATCH("/api/v1/job-occupations/{id}/status")
    suspend fun updateStatus(
        @Path("id") id: Long,
        @Query("active") active: Boolean
    ): Response<JobOccupationRes>

    @DELETE("/api/v1/job-occupations/{id}")
    suspend fun deleteOccupation(@Path("id") id: Long): Response<Unit>
}
