package com.example.nexum_trabajador.data.worker.remote

import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import com.example.nexum_trabajador.data.worker.remote.payload.req.UpdateWorkerReq
import com.example.nexum_trabajador.data.worker.remote.payload.res.WorkerRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/18/2025
 * @version 1.0
 */
interface WorkerApi {
    @GET("/api/v1/workers")
    suspend fun getWorker(): Response<WorkerRes>

    @PUT("/api/v1/workers")
    suspend fun updateWorkerDetails(@Body req: UpdateWorkerReq): Response<MessageRes>

    @PATCH("/api/v1/workers/availability")
    suspend fun setAvailability(@Query("available") available: Boolean): Response<WorkerRes>
}