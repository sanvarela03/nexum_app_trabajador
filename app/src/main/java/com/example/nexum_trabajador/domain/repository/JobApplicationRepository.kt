package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddJobApplicationReq
import com.example.nexum_trabajador.data.job_application.remote.payload.res.JobApplicationRes
import com.example.nexum_trabajador.domain.model.JobApplicationDraft

interface JobApplicationRepository {
    suspend fun applyToJobOffer(req: AddJobApplicationReq): Result<Unit>
    suspend fun getWorkerJobApplications(): Result<List<JobApplicationRes>>
    suspend fun getDraft(jobOfferId: Long): JobApplicationDraft?
    suspend fun getAllOtherDrafts(currentJobOfferId: Long): List<JobApplicationDraft>
    suspend fun saveDraft(draft: JobApplicationDraft)
    suspend fun deleteDraft(jobOfferId: Long)
    suspend fun deleteDraftByUuid(jobOfferUuid: String)
}
