package com.example.nexum_trabajador.data.contract.remote

import com.example.nexum_trabajador.data.contract.remote.payload.req.GenerateInvoiceReq
import com.example.nexum_trabajador.data.contract.remote.payload.res.ContractRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
interface ContractApi {

    @GET("/api/v1/contracts/my-contracts")
    suspend fun getMyContracts(): Response<List<ContractRes>>

    @GET("/api/v1/contracts/{id}")
    suspend fun getContractById(@Path("id") id: Long): Response<ContractRes>

    @POST("/api/v1/contracts/{id}/verify-meeting")
    suspend fun verifyMeeting(
        @Path("id") id: Long,
        @Query("code") code: String
    ): Response<ContractRes>

    @POST("/api/v1/contracts/{id}/invoice")
    suspend fun generateInvoice(
        @Path("id") id: Long,
        @Body req: GenerateInvoiceReq
    ): Response<ContractRes>

    @POST("/api/v1/contracts/{id}/complete-by-worker")
    suspend fun completeByWorker(@Path("id") id: Long): Response<ContractRes>

    @POST("/api/v1/contracts/{id}/schedule-meeting")
    suspend fun scheduleMeeting(
        @Path("id") id: Long,
        @Query("date") date: String,
        @Query("h3Index") h3Index: String
    ): Response<ContractRes>
}
