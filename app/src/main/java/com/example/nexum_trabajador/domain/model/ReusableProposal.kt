package com.example.nexum_trabajador.domain.model

import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddMaterialReq
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddTaskReq

data class ReusableProposal(
    val id: String,
    val title: String,
    val proposalText: String,
    val laborCost: Double,
    val workWarrantyInDays: Int,
    val materials: List<AddMaterialReq>,
    val tasks: List<AddTaskReq>,
    val isDraft: Boolean,
    val dateFormatted: String
)
