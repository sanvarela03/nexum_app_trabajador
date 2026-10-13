package com.example.nexum_trabajador.ui.presenter.insurance

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.insurance.remote.payload.req.CreateInsuranceReq
import com.example.nexum_trabajador.domain.repository.InsuranceRepository
import com.example.nexum_trabajador.domain.use_cases.common.MediaManagementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsurancesViewModel @Inject constructor(
    private val insuranceRepository: InsuranceRepository,
    private val mediaManagementUseCase: MediaManagementUseCase
) : ViewModel() {

    var state by mutableStateOf(InsurancesState())
        private set

    init {
        loadInsurances()
    }

    fun onEvent(event: InsurancesEvent) {
        when (event) {
            is InsurancesEvent.Refresh -> loadInsurances()
            is InsurancesEvent.OpenAddDialog -> {
                state = state.copy(
                    showAddDialog = true,
                    insuranceName = "",
                    coverageType = "",
                    description = "",
                    selectedFileUri = null,
                    uploadedFileUrl = ""
                )
            }
            is InsurancesEvent.DismissDialog -> {
                state = state.copy(showAddDialog = false)
            }
            is InsurancesEvent.NameChanged -> {
                state = state.copy(insuranceName = event.name)
            }
            is InsurancesEvent.CoverageTypeChanged -> {
                state = state.copy(coverageType = event.type)
            }
            is InsurancesEvent.DescriptionChanged -> {
                state = state.copy(description = event.desc)
            }
            is InsurancesEvent.FileSelected -> {
                state = state.copy(selectedFileUri = event.uri)
                if (event.uri != null) {
                    uploadFile(event.uri)
                }
            }
            is InsurancesEvent.SubmitCreate -> createInsurance()
            is InsurancesEvent.DeleteInsurance -> deleteInsurance(event.id)
            is InsurancesEvent.ClearMessages -> {
                state = state.copy(errorMessage = null, successMessage = null)
            }
        }
    }

    private fun loadInsurances() {
        viewModelScope.launch {
            insuranceRepository.getMyInsurances().collect { response ->
                when (response) {
                    is ApiResponse.Loading -> state = state.copy(isLoading = true)
                    is ApiResponse.Success -> state = state.copy(isLoading = false, insurances = response.data)
                    is ApiResponse.Error -> state = state.copy(isLoading = false, errorMessage = response.errorMessage)
                    is ApiResponse.Failure -> state = state.copy(isLoading = false, errorMessage = response.errorMessage)
                }
            }
        }
    }

    private fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            state = state.copy(isUploadingFile = true)
            mediaManagementUseCase.uploadAndSaveImage(
                uri = uri,
                oldUrl = "",
                storageKey = "insurance_file_${System.currentTimeMillis()}",
                folder = "insurances"
            ).onSuccess { downloadUrl ->
                state = state.copy(
                    isUploadingFile = false,
                    uploadedFileUrl = downloadUrl,
                    successMessage = "Documento subido correctamente"
                )
            }.onFailure { ex ->
                state = state.copy(
                    isUploadingFile = false,
                    errorMessage = "Error al subir documento: ${ex.message}"
                )
            }
        }
    }

    private fun createInsurance() {
        val name = state.insuranceName.trim()
        if (name.isBlank()) {
            state = state.copy(errorMessage = "Ingresa el nombre de la aseguradora o póliza")
            return
        }

        viewModelScope.launch {
            val req = CreateInsuranceReq(
                name = name,
                coverageType = state.coverageType.takeIf { it.isNotBlank() },
                description = state.description.takeIf { it.isNotBlank() },
                insuranceUrl = state.uploadedFileUrl.takeIf { it.isNotBlank() }
            )

            insuranceRepository.createInsurance(req).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> state = state.copy(isSaving = true)
                    is ApiResponse.Success -> {
                        state = state.copy(
                            isSaving = false,
                            showAddDialog = false,
                            successMessage = "Póliza registrada exitosamente"
                        )
                        loadInsurances()
                    }
                    is ApiResponse.Error -> state = state.copy(isSaving = false, errorMessage = response.errorMessage)
                    is ApiResponse.Failure -> state = state.copy(isSaving = false, errorMessage = response.errorMessage)
                }
            }
        }
    }

    private fun deleteInsurance(id: Long) {
        viewModelScope.launch {
            insuranceRepository.deleteInsurance(id).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        state = state.copy(
                            insurances = state.insurances.filter { it.id != id },
                            successMessage = "Póliza eliminada"
                        )
                    }
                    is ApiResponse.Error -> state = state.copy(errorMessage = response.errorMessage)
                    is ApiResponse.Failure -> state = state.copy(errorMessage = response.errorMessage)
                    else -> Unit
                }
            }
        }
    }
}
