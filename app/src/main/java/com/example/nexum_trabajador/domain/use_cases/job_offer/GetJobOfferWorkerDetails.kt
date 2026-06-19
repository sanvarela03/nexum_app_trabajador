package com.example.nexum_trabajador.domain.use_cases.job_offer

import com.example.nexum_trabajador.domain.repository.JobOfferRepository
import javax.inject.Inject

class GetJobOfferWorkerDetails @Inject constructor(
    private val repository: JobOfferRepository
) {
    operator fun invoke(jobOfferUuid: String) = repository.getJobOfferWorkerDetails(jobOfferUuid)
}
