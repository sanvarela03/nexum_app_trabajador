package com.example.nexum_trabajador.ui.presenter.occupations

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.certification.remote.payload.req.CreateCertificationReq
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.CreateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import com.example.nexum_trabajador.data.skill_set.remote.payload.req.AddSkillsReq
import com.example.nexum_trabajador.domain.repository.CategoryRepository
import com.example.nexum_trabajador.domain.repository.CertificationRepository
import com.example.nexum_trabajador.domain.repository.JobOccupationRepository
import com.example.nexum_trabajador.domain.repository.SkillSetRepository
import com.example.nexum_trabajador.domain.use_cases.common.MediaManagementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OccupationsViewModel @Inject constructor(
    private val jobOccupationRepository: JobOccupationRepository,
    private val categoryRepository: CategoryRepository,
    private val skillSetRepository: SkillSetRepository,
    private val certificationRepository: CertificationRepository,
    private val mediaManagementUseCase: MediaManagementUseCase
) : ViewModel() {

    var state by mutableStateOf(OccupationsState())
        private set

    init {
        loadData()
    }

    fun onEvent(event: OccupationsEvent) {
        when (event) {
            is OccupationsEvent.Refresh -> loadData()
            is OccupationsEvent.OpenAddDialog -> {
                state = state.copy(
                    showAddDialog = true,
                    selectedCategory = null,
                    occupationName = "",
                    occupationDescription = "",
                    yearsExperience = ""
                )
            }
            is OccupationsEvent.DismissDialog -> {
                state = state.copy(showAddDialog = false)
            }
            is OccupationsEvent.CategorySelected -> {
                state = state.copy(
                    selectedCategory = event.category,
                    occupationName = if (state.occupationName.isBlank()) event.category.name ?: "" else state.occupationName
                )
            }
            is OccupationsEvent.NameChanged -> {
                state = state.copy(occupationName = event.name)
            }
            is OccupationsEvent.DescriptionChanged -> {
                state = state.copy(occupationDescription = event.description)
            }
            is OccupationsEvent.YearsExperienceChanged -> {
                state = state.copy(yearsExperience = event.years)
            }
            is OccupationsEvent.SubmitCreate -> createOccupation()
            is OccupationsEvent.ToggleStatus -> toggleStatus(event.occupation)
            is OccupationsEvent.DeleteOccupation -> deleteOccupation(event.id)

            // Skill events
            is OccupationsEvent.OpenAddSkillDialog -> {
                state = state.copy(
                    showAddSkillDialog = true,
                    targetOccupationIdForSkill = event.occupationId,
                    newSkillName = ""
                )
            }
            is OccupationsEvent.DismissSkillDialog -> {
                state = state.copy(
                    showAddSkillDialog = false,
                    targetOccupationIdForSkill = null,
                    newSkillName = ""
                )
            }
            is OccupationsEvent.SkillNameChanged -> {
                state = state.copy(newSkillName = event.name)
            }
            is OccupationsEvent.SubmitAddSkill -> addSkill()
            is OccupationsEvent.RemoveSkill -> removeSkill(event.occupationId, event.skillId)

            // Certification events
            is OccupationsEvent.OpenAddCertDialog -> {
                state = state.copy(
                    showAddCertDialog = true,
                    targetOccupationIdForCert = event.occupationId,
                    certName = "",
                    certIssuedBy = "",
                    certFileUri = null,
                    certUploadedUrl = null
                )
            }
            is OccupationsEvent.DismissCertDialog -> {
                state = state.copy(
                    showAddCertDialog = false,
                    targetOccupationIdForCert = null,
                    certName = "",
                    certIssuedBy = "",
                    certFileUri = null,
                    certUploadedUrl = null
                )
            }
            is OccupationsEvent.CertNameChanged -> {
                state = state.copy(certName = event.name)
            }
            is OccupationsEvent.CertIssuedByChanged -> {
                state = state.copy(certIssuedBy = event.issuedBy)
            }
            is OccupationsEvent.CertFileSelected -> uploadCertFile(event.uri)
            is OccupationsEvent.SubmitAddCert -> addCertification()
            is OccupationsEvent.DeleteCert -> deleteCertification(event.occupationId, event.certId)

            is OccupationsEvent.ClearMessages -> {
                state = state.copy(errorMessage = null, successMessage = null)
            }
        }
    }

    private fun loadData() {
        loadOccupations()
        loadCategories()
    }

    private fun loadOccupations() {
        viewModelScope.launch {
            jobOccupationRepository.getMyOccupations().collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        state = state.copy(isLoading = true)
                    }
                    is ApiResponse.Success -> {
                        state = state.copy(
                            isLoading = false,
                            occupations = response.data
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(
                            isLoading = false,
                            errorMessage = response.errorMessage
                        )
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(
                            isLoading = false,
                            errorMessage = response.errorMessage
                        )
                    }
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        state = state.copy(categories = response.data)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun createOccupation() {
        val category = state.selectedCategory
        val name = state.occupationName.trim()
        if (category == null) {
            state = state.copy(errorMessage = "Selecciona una categoría")
            return
        }
        if (name.isBlank()) {
            state = state.copy(errorMessage = "Ingresa un título o nombre para la profesión")
            return
        }

        val years = state.yearsExperience.toIntOrNull()

        viewModelScope.launch {
            val req = CreateJobOccupationReq(
                categoryId = category.id,
                name = name,
                description = state.occupationDescription.takeIf { it.isNotBlank() },
                yearsExperience = years
            )

            jobOccupationRepository.createOccupation(req).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        state = state.copy(isSaving = true)
                    }
                    is ApiResponse.Success -> {
                        state = state.copy(
                            isSaving = false,
                            showAddDialog = false,
                            successMessage = "¡Profesión agregada exitosamente!"
                        )
                        loadOccupations()
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(
                            isSaving = false,
                            errorMessage = response.errorMessage
                        )
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(
                            isSaving = false,
                            errorMessage = response.errorMessage
                        )
                    }
                }
            }
        }
    }

    private fun toggleStatus(occupation: JobOccupationRes) {
        val newStatus = !(occupation.isActive ?: true)
        viewModelScope.launch {
            jobOccupationRepository.updateStatus(occupation.id, newStatus).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        state = state.copy(
                            occupations = state.occupations.map {
                                if (it.id == occupation.id) it.copy(isActive = newStatus) else it
                            }
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(errorMessage = response.errorMessage)
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(errorMessage = response.errorMessage)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun deleteOccupation(id: Long) {
        viewModelScope.launch {
            jobOccupationRepository.deleteOccupation(id).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        state = state.copy(
                            occupations = state.occupations.filter { it.id != id },
                            successMessage = "Profesión eliminada"
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(errorMessage = response.errorMessage)
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(errorMessage = response.errorMessage)
                    }
                    else -> Unit
                }
            }
        }
    }

    // --- SKILLS METHODS ---
    private fun addSkill() {
        val occupationId = state.targetOccupationIdForSkill ?: return
        val skillName = state.newSkillName.trim()
        if (skillName.isBlank()) {
            state = state.copy(errorMessage = "Ingresa el nombre de la habilidad")
            return
        }

        viewModelScope.launch {
            val req = AddSkillsReq(skills = listOf(skillName))
            skillSetRepository.addSkillsToOccupation(occupationId, req).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        state = state.copy(isSavingSkill = true)
                    }
                    is ApiResponse.Success -> {
                        val updated = state.occupations.map {
                            if (it.id == occupationId) it.copy(skills = response.data) else it
                        }
                        state = state.copy(
                            isSavingSkill = false,
                            showAddSkillDialog = false,
                            targetOccupationIdForSkill = null,
                            newSkillName = "",
                            occupations = updated,
                            successMessage = "Habilidad agregada exitosamente"
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(isSavingSkill = false, errorMessage = response.errorMessage)
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(isSavingSkill = false, errorMessage = response.errorMessage)
                    }
                }
            }
        }
    }

    private fun removeSkill(occupationId: Long, skillId: Long) {
        viewModelScope.launch {
            skillSetRepository.removeSkillFromOccupation(occupationId, skillId).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val updated = state.occupations.map {
                            if (it.id == occupationId) it.copy(skills = response.data) else it
                        }
                        state = state.copy(occupations = updated, successMessage = "Habilidad eliminada")
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(errorMessage = response.errorMessage)
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(errorMessage = response.errorMessage)
                    }
                    else -> Unit
                }
            }
        }
    }

    // --- CERTIFICATION METHODS ---
    private fun uploadCertFile(uri: Uri) {
        viewModelScope.launch {
            state = state.copy(isUploadingCertFile = true, certFileUri = uri)
            mediaManagementUseCase.uploadAndSaveImage(
                uri = uri,
                oldUrl = "",
                storageKey = "cert_${System.currentTimeMillis()}",
                folder = "certifications"
            ).onSuccess { downloadUrl ->
                state = state.copy(
                    isUploadingCertFile = false,
                    certUploadedUrl = downloadUrl,
                    successMessage = "Archivo de certificado subido con éxito"
                )
            }.onFailure { ex ->
                state = state.copy(
                    isUploadingCertFile = false,
                    errorMessage = "Error al subir certificado: ${ex.message}"
                )
            }
        }
    }

    private fun addCertification() {
        val occupationId = state.targetOccupationIdForCert ?: return
        val name = state.certName.trim()
        val url = state.certUploadedUrl

        if (name.isBlank()) {
            state = state.copy(errorMessage = "Ingresa el nombre del certificado")
            return
        }
        if (url.isNullOrBlank()) {
            state = state.copy(errorMessage = "Por favor adjunta el documento o imagen del certificado")
            return
        }

        viewModelScope.launch {
            val req = CreateCertificationReq(
                jobOccupationId = occupationId,
                name = name,
                certificationUrl = url,
                issuedBy = state.certIssuedBy.trim().takeIf { it.isNotBlank() }
            )

            certificationRepository.createCertification(req).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        state = state.copy(isSavingCert = true)
                    }
                    is ApiResponse.Success -> {
                        val updated = state.occupations.map {
                            if (it.id == occupationId) it.copy(certifications = it.certifications + response.data) else it
                        }
                        state = state.copy(
                            isSavingCert = false,
                            showAddCertDialog = false,
                            targetOccupationIdForCert = null,
                            certName = "",
                            certIssuedBy = "",
                            certFileUri = null,
                            certUploadedUrl = null,
                            occupations = updated,
                            successMessage = "Certificado agregado exitosamente"
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(isSavingCert = false, errorMessage = response.errorMessage)
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(isSavingCert = false, errorMessage = response.errorMessage)
                    }
                }
            }
        }
    }

    private fun deleteCertification(occupationId: Long, certId: Long) {
        viewModelScope.launch {
            certificationRepository.deleteCertification(certId).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        val updated = state.occupations.map {
                            if (it.id == occupationId) {
                                it.copy(certifications = it.certifications.filter { c -> c.id != certId })
                            } else it
                        }
                        state = state.copy(occupations = updated, successMessage = "Certificado eliminado")
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(errorMessage = response.errorMessage)
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(errorMessage = response.errorMessage)
                    }
                    else -> Unit
                }
            }
        }
    }
}
