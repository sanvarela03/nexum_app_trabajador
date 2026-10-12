package com.example.nexum_trabajador.domain.use_cases.worker

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class WorkerUseCases @Inject constructor(
    val updateWorker: UpdateWorker,
    val observeWorker: ObserveWorker,
    val updateWorkerDetails: UpdateWorkerDetails,
    val setWorkerAvailability: SetWorkerAvailabilityUseCase
)

