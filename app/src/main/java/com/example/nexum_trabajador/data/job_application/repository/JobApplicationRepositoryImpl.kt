package com.example.nexum_trabajador.data.job_application.repository

import com.example.nexum_trabajador.data.job_application.remote.JobApplicationApi
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddJobApplicationReq
import com.example.nexum_trabajador.data.job_application.local.JobApplicationDraftDao
import com.example.nexum_trabajador.data.job_application.local.JobApplicationDraftEntity
import com.example.nexum_trabajador.domain.repository.JobApplicationRepository
import com.example.nexum_trabajador.domain.model.JobApplicationDraft
import javax.inject.Inject

class JobApplicationRepositoryImpl @Inject constructor(
    private val api: JobApplicationApi,
    private val draftDao: JobApplicationDraftDao
) : JobApplicationRepository {

    override suspend fun applyToJobOffer(req: AddJobApplicationReq): Result<Unit> {
        return try {
            val response = api.addJobApplication(req)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message() ?: "Error al enviar la aplicación"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDraft(jobOfferId: Long): JobApplicationDraft? {
        return draftDao.getDraft(jobOfferId)?.let { entity ->
            JobApplicationDraft(
                jobOfferId = entity.jobOfferId,
                jobOfferUuid = entity.jobOfferUuid,
                proposalText = entity.proposalText,
                laborCost = entity.laborCost,
                workWarrantyInDays = entity.workWarrantyInDays,
                materialsJson = entity.materialsJson,
                tasksJson = entity.tasksJson,
                workSchedulesJson = entity.workSchedulesJson
            )
        }
    }

    override suspend fun saveDraft(draft: JobApplicationDraft) {
        val entity = JobApplicationDraftEntity(
            jobOfferId = draft.jobOfferId,
            jobOfferUuid = draft.jobOfferUuid,
            proposalText = draft.proposalText,
            laborCost = draft.laborCost,
            workWarrantyInDays = draft.workWarrantyInDays,
            materialsJson = draft.materialsJson,
            tasksJson = draft.tasksJson,
            workSchedulesJson = draft.workSchedulesJson
        )
        draftDao.saveDraft(entity)
    }

    override suspend fun deleteDraft(jobOfferId: Long) {
        draftDao.deleteDraft(jobOfferId)
    }

    override suspend fun deleteDraftByUuid(jobOfferUuid: String) {
        draftDao.deleteDraftByUuid(jobOfferUuid)
    }
}

