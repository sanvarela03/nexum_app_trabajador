package com.example.nexum_trabajador.ui.presenter.occupations

import com.example.nexum_trabajador.data.category.remote.payload.res.CategoryRes
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes

sealed class OccupationsEvent {
    object Refresh : OccupationsEvent()
    object OpenAddDialog : OccupationsEvent()
    object DismissDialog : OccupationsEvent()
    data class CategorySelected(val category: CategoryRes) : OccupationsEvent()
    data class NameChanged(val name: String) : OccupationsEvent()
    data class DescriptionChanged(val description: String) : OccupationsEvent()
    data class YearsExperienceChanged(val years: String) : OccupationsEvent()
    object SubmitCreate : OccupationsEvent()
    data class ToggleStatus(val occupation: JobOccupationRes) : OccupationsEvent()
    data class DeleteOccupation(val id: Long) : OccupationsEvent()
    object ClearMessages : OccupationsEvent()
}
