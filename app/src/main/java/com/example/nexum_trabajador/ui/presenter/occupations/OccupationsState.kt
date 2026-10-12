package com.example.nexum_trabajador.ui.presenter.occupations

import com.example.nexum_trabajador.data.category.remote.payload.res.CategoryRes
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes

data class OccupationsState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val occupations: List<JobOccupationRes> = emptyList(),
    val categories: List<CategoryRes> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    // Add dialog state
    val showAddDialog: Boolean = false,
    val selectedCategory: CategoryRes? = null,
    val occupationName: String = "",
    val occupationDescription: String = "",
    val yearsExperience: String = ""
)
