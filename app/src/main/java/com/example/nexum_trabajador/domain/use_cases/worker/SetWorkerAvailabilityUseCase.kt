package com.example.nexum_trabajador.domain.use_cases.worker

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.worker.remote.payload.res.WorkerRes
import com.example.nexum_trabajador.domain.repository.WorkerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/28/2026
 * @version 1.0
 */
class SetWorkerAvailabilityUseCase @Inject constructor(
    private val repository: WorkerRepository
) {
    operator fun invoke(available: Boolean): Flow<ApiResponse<WorkerRes>> =
        repository.setAvailability(available)
}
