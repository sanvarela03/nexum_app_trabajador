package com.example.nexum_trabajador.domain.use_cases.worker

import com.example.nexum_trabajador.domain.model.Worker
import com.example.nexum_trabajador.domain.repository.WorkerRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/17/2026
 * @version 1.0
 */
class UpdateWorkerDetails @Inject constructor(
    private val repository: WorkerRepository
) {
    operator fun invoke(worker: Worker) = repository.updateWorkerDetails(worker)
}