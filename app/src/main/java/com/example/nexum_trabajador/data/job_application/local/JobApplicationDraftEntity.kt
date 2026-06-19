package com.example.nexum_trabajador.data.job_application.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JobApplicationDraftEntity(
    @PrimaryKey
    val jobOfferId: Long,
    val jobOfferUuid: String?,
    val proposalText: String,
    val laborCost: Double,
    val workWarrantyInDays: Int,
    val materialsJson: String,
    val tasksJson: String,
    val workSchedulesJson: String
)
