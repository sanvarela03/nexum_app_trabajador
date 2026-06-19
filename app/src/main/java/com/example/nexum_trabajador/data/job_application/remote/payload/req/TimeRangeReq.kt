package com.example.nexum_trabajador.data.job_application.remote.payload.req

import com.google.gson.annotations.SerializedName

data class TimeRangeReq(
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String
)
