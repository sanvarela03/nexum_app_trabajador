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
    // Skill events
    data class OpenAddSkillDialog(val occupationId: Long) : OccupationsEvent()
    object DismissSkillDialog : OccupationsEvent()
    data class SkillNameChanged(val name: String) : OccupationsEvent()
    object SubmitAddSkill : OccupationsEvent()
    data class RemoveSkill(val occupationId: Long, val skillId: Long) : OccupationsEvent()

    // Certification events
    data class OpenAddCertDialog(val occupationId: Long) : OccupationsEvent()
    object DismissCertDialog : OccupationsEvent()
    data class CertNameChanged(val name: String) : OccupationsEvent()
    data class CertIssuedByChanged(val issuedBy: String) : OccupationsEvent()
    data class CertFileSelected(val uri: android.net.Uri) : OccupationsEvent()
    object SubmitAddCert : OccupationsEvent()
    data class DeleteCert(val occupationId: Long, val certId: Long) : OccupationsEvent()

    object ClearMessages : OccupationsEvent()
}
