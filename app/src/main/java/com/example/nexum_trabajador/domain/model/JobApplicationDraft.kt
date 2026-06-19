package com.example.nexum_trabajador.domain.model

data class JobApplicationDraft(
    val jobOfferId: Long,
    val jobOfferUuid: String?,
    val proposalText: String,
    val laborCost: Double,
    val workWarrantyInDays: Int,
    val materialsJson: String,
    val tasksJson: String,
    val workSchedulesJson: String
)
