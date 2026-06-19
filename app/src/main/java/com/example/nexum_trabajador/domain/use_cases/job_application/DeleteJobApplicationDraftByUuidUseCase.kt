package com.example.nexum_trabajador.domain.use_cases.job_application

import com.example.nexum_trabajador.domain.repository.JobApplicationRepository
import javax.inject.Inject

class DeleteJobApplicationDraftByUuidUseCase @Inject constructor(
    private val repository: JobApplicationRepository
) {
    suspend operator fun invoke(jobOfferUuid: String) {
        repository.deleteDraftByUuid(jobOfferUuid)
    }
}

