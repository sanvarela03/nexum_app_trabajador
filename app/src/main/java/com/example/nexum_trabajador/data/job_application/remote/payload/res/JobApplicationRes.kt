package com.example.nexum_trabajador.data.job_application.remote.payload.res

import com.google.gson.annotations.SerializedName

data class JobApplicationRes(
    @SerializedName("job_application_id")
    val jobApplicationId: Long? = null,
    @SerializedName("job_offer")
    val jobOffer: JobApplicationOfferRes? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("labor_cost")
    val laborCost: Double? = null,
    @SerializedName("proposal_text")
    val proposalText: String? = null,
    @SerializedName("work_warranty_in_days")
    val workWarrantyInDays: Int? = null,
    @SerializedName("estimated_time_in_minutes")
    val estimatedTimeInMinutes: Double? = null,
    @SerializedName("materials_cost")
    val materialsCost: Double? = null,
    @SerializedName("total_cost")
    val totalCost: Double? = null,
    @SerializedName("materials")
    val materials: List<MaterialRes>? = null,
    @SerializedName("tasks")
    val tasks: List<TaskRes>? = null,
    @SerializedName("applied_at")
    val appliedAt: String? = null
)

data class JobApplicationOfferRes(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null
)

data class MaterialRes(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("unitPrice")
    val unitPrice: Double? = null
)

data class TaskRes(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("taskName")
    val taskName: String? = null,
    @SerializedName("taskDescription")
    val taskDescription: String? = null
)
