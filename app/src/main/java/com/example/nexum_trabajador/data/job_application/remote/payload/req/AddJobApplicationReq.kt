package com.example.nexum_trabajador.data.job_application.remote.payload.req

import com.google.gson.annotations.SerializedName

data class AddJobApplicationReq(
    @SerializedName("job_offer_id") val jobOfferId: Long,
    @SerializedName("scheduled_date") val scheduledDate: String,
    @SerializedName("estimated_time_in_minutes") val estimatedTimeInMinutes: Double,
    @SerializedName("labor_cost") val laborCost: Double,
    @SerializedName("work_warranty_in_days") val workWarrantyInDays: Int,
    @SerializedName("proposal_text") val proposalText: String,
    @SerializedName("materials") val materials: List<AddMaterialReq>,
    @SerializedName("tasks") val tasks: List<AddTaskReq>,
    @SerializedName("work_schedules") val workSchedules: Map<String, List<TimeRangeReq>>
)
