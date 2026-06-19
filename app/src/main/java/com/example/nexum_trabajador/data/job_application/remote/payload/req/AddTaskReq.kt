package com.example.nexum_trabajador.data.job_application.remote.payload.req

import com.google.gson.annotations.SerializedName

data class AddTaskReq(
    @SerializedName("taskName") val taskName: String,
    @SerializedName("taskDescription") val taskDescription: String
)
