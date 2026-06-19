package com.example.nexum_trabajador.domain.use_cases.worker

import com.example.nexum_trabajador.domain.repository.WorkerRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/18/2025
 * @version 1.0
 */
class UpdateWorker @Inject constructor(
    private val repository: WorkerRepository
) {
    operator fun invoke(fetchFromRemote: Boolean) = repository.update(fetchFromRemote)
}