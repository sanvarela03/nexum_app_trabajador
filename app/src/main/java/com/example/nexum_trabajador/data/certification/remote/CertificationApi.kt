package com.example.nexum_trabajador.data.certification.remote

import com.example.nexum_trabajador.data.certification.remote.payload.req.CreateCertificationReq
import com.example.nexum_trabajador.data.certification.remote.payload.res.CertificationRes
import retrofit2.Response
import retrofit2.http.*

interface CertificationApi {
    @GET("/api/v1/certifications/by-occupation/{occupationId}")
    suspend fun getCertificationsByOccupation(@Path("occupationId") occupationId: Long): Response<List<CertificationRes>>

    @POST("/api/v1/certifications")
    suspend fun createCertification(@Body req: CreateCertificationReq): Response<CertificationRes>

    @DELETE("/api/v1/certifications/{id}")
    suspend fun deleteCertification(@Path("id") id: Long): Response<Unit>
}
