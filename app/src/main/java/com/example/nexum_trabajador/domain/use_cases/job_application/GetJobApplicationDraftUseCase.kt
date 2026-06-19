package com.example.nexum_trabajador.domain.use_cases.job_application

import com.example.nexum_trabajador.domain.model.JobApplicationDraft
import com.example.nexum_trabajador.domain.repository.JobApplicationRepository
import javax.inject.Inject

class GetJobApplicationDraftUseCase @Inject constructor(
    private val repository: JobApplicationRepository
) {
    suspend operator fun invoke(jobOfferId: Long): JobApplicationDraft? {
        return repository.getDraft(jobOfferId)
    }
}

