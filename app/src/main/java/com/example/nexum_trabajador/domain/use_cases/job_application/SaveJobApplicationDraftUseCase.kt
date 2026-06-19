package com.example.nexum_trabajador.domain.use_cases.job_application

import com.example.nexum_trabajador.domain.model.JobApplicationDraft
import com.example.nexum_trabajador.domain.repository.JobApplicationRepository
import javax.inject.Inject

class SaveJobApplicationDraftUseCase @Inject constructor(
    private val repository: JobApplicationRepository
) {
    suspend operator fun invoke(draft: JobApplicationDraft) {
        repository.saveDraft(draft)
    }
}

