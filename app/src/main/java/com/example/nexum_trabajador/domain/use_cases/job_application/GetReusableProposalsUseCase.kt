package com.example.nexum_trabajador.domain.use_cases.job_application

import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddMaterialReq
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddTaskReq
import com.example.nexum_trabajador.domain.model.ReusableProposal
import com.example.nexum_trabajador.domain.repository.JobApplicationRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetReusableProposalsUseCase @Inject constructor(
    private val repository: JobApplicationRepository
) {
    suspend operator fun invoke(currentJobOfferId: Long): List<ReusableProposal> {
        val reusableList = mutableListOf<ReusableProposal>()

        // 1. Fetch Room Drafts from other job offers
        try {
            val otherDrafts = repository.getAllOtherDrafts(currentJobOfferId)
            val materialsType = object : TypeToken<List<AddMaterialReq>>() {}.type
            val tasksType = object : TypeToken<List<AddTaskReq>>() {}.type
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

            for (draft in otherDrafts) {
                // Ignore empty drafts
                if (draft.proposalText.isBlank() && draft.laborCost <= 0.0) continue

                val materials: List<AddMaterialReq> = try {
                    Gson().fromJson(draft.materialsJson, materialsType) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }

                val tasks: List<AddTaskReq> = try {
                    Gson().fromJson(draft.tasksJson, tasksType) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }

                val title = draft.jobOfferTitle?.takeIf { it.isNotBlank() }
                    ?: "Borrador de Oferta #${draft.jobOfferId}"

                reusableList.add(
                    ReusableProposal(
                        id = "draft_${draft.jobOfferId}",
                        title = title,
                        proposalText = draft.proposalText,
                        laborCost = draft.laborCost,
                        workWarrantyInDays = draft.workWarrantyInDays,
                        materials = materials,
                        tasks = tasks,
                        isDraft = true,
                        dateFormatted = dateFormat.format(Date(draft.updatedAt))
                    )
                )
            }
        } catch (_: Exception) {}

        // 2. Fetch remote past Job Applications
        try {
            val appsResult = repository.getWorkerJobApplications()
            appsResult.getOrNull()?.let { applications ->
                for (app in applications) {
                    val proposalText = app.proposalText.orEmpty()
                    val laborCost = app.laborCost ?: 0.0

                    if (proposalText.isBlank() && laborCost <= 0.0) continue

                    val materials = app.materials?.map { m ->
                        AddMaterialReq(
                            name = m.name.orEmpty(),
                            description = m.description.orEmpty(),
                            type = m.type ?: "TOOL",
                            quantity = m.quantity ?: 1,
                            unitPrice = m.unitPrice ?: 0.0
                        )
                    } ?: emptyList()

                    val tasks = app.tasks?.map { t ->
                        AddTaskReq(
                            taskName = t.taskName.orEmpty(),
                            taskDescription = t.taskDescription.orEmpty()
                        )
                    } ?: emptyList()

                    val title = app.jobOffer?.title?.takeIf { it.isNotBlank() }
                        ?: "Cotización #${app.jobApplicationId ?: ""}"

                    val dateStr = if (!app.appliedAt.isNullOrBlank()) {
                        try {
                            if (app.appliedAt.contains("T")) {
                                val parts = app.appliedAt.split("T")
                                val datePart = parts[0]
                                val timePart = parts[1].take(5)
                                "$datePart $timePart"
                            } else {
                                app.appliedAt
                            }
                        } catch (e: Exception) {
                            app.appliedAt
                        }
                    } else {
                        "Enviada"
                    }

                    reusableList.add(
                        ReusableProposal(
                            id = "app_${app.jobApplicationId ?: app.hashCode()}",
                            title = title,
                            proposalText = proposalText,
                            laborCost = laborCost,
                            workWarrantyInDays = app.workWarrantyInDays ?: 0,
                            materials = materials,
                            tasks = tasks,
                            isDraft = false,
                            dateFormatted = dateStr
                        )
                    )
                }
            }
        } catch (_: Exception) {}

        return reusableList
    }
}
