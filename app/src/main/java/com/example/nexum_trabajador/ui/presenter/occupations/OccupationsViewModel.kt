package com.example.nexum_trabajador.ui.presenter.occupations

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.job_occupation.remote.payload.req.CreateJobOccupationReq
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import com.example.nexum_trabajador.domain.repository.CategoryRepository
import com.example.nexum_trabajador.domain.repository.JobOccupationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OccupationsViewModel @Inject constructor(
    private val jobOccupationRepository: JobOccupationRepository,
    private val categoryRepository: CategoryRepository
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
}
