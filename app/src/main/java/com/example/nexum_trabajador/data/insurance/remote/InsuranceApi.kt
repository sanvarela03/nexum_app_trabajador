package com.example.nexum_trabajador.data.insurance.remote

import com.example.nexum_trabajador.data.insurance.remote.payload.req.CreateInsuranceReq
import com.example.nexum_trabajador.data.insurance.remote.payload.res.InsuranceRes
import retrofit2.Response
import retrofit2.http.*

interface InsuranceApi {
    @GET("/api/v1/insurances/my-insurances")
    suspend fun getMyInsurances(): Response<List<InsuranceRes>>

    @POST("/api/v1/insurances")
    suspend fun createInsurance(@Body req: CreateInsuranceReq): Response<InsuranceRes>

    @DELETE("/api/v1/insurances/{id}")
    suspend fun deleteInsurance(@Path("id") id: Long): Response<Unit>
}
