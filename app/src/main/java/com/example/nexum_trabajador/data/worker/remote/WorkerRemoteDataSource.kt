package com.example.nexum_trabajador.data.worker.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import com.example.nexum_trabajador.data.worker.remote.payload.req.UpdateWorkerReq
import com.example.nexum_trabajador.data.worker.remote.payload.res.WorkerRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/18/2025
 * @version 1.0
 */
@Singleton
class WorkerRemoteDataSource @Inject constructor(
    private val api: WorkerApi
) {
    fun getWorkers(): Flow<ApiResponse<WorkerRes>> =
        apiRequestFlow { api.getWorker() }

    fun updateWorkerDetails(req: UpdateWorkerReq): Flow<ApiResponse<MessageRes>> =
        apiRequestFlow { api.updateWorkerDetails(req) }

    fun setAvailability(available: Boolean): Flow<ApiResponse<WorkerRes>> =
        apiRequestFlow { api.setAvailability(available) }
}