package com.example.nexum_trabajador.domain.use_cases.job_application

import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddJobApplicationReq
import com.example.nexum_trabajador.domain.repository.JobApplicationRepository
import javax.inject.Inject

class ApplyToJobOfferUseCase @Inject constructor(
    private val repository: JobApplicationRepository
) {
    suspend operator fun invoke(req: AddJobApplicationReq): Result<Unit> {
        return repository.applyToJobOffer(req)
    }
}

