package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import com.example.nexum_trabajador.domain.model.Worker
import kotlinx.coroutines.flow.Flow

// Placeholder for WorkerRepository
interface WorkerRepository {
    // TODO: Add worker repository methods
    fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>>
    fun observe(): Flow<Worker?>
    fun updateWorkerDetails(worker: Worker): Flow<ApiResponse<MessageRes>>
}
