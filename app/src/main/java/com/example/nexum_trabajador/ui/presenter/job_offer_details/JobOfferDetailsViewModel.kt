package com.example.nexum_trabajador.ui.presenter.job_offer_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.use_cases.job_offer.JobOfferUseCases
import com.example.nexum_trabajador.domain.use_cases.job_application.DeleteJobApplicationDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobOfferDetailsViewModel @Inject constructor(
    private val useCases: JobOfferUseCases,
    private val deleteJobApplicationDraftUseCase: DeleteJobApplicationDraftUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(JobOfferDetailsState())
    val state = _state.asStateFlow()

    fun discardDraft(jobOfferId: Long) {
        viewModelScope.launch {
            deleteJobApplicationDraftUseCase(jobOfferId)
        }
    }

    fun onEvent(event: JobOfferDetailsEvent) {
        when (event) {
            is JobOfferDetailsEvent.FetchDetails -> fetchDetails(event.uuid)
            is JobOfferDetailsEvent.ApplyClicked -> _state.update { it.copy(showApplyDialog = true) }
            is JobOfferDetailsEvent.DismissDialog -> _state.update { it.copy(showApplyDialog = false) }
            is JobOfferDetailsEvent.ConfirmApply -> {
                _state.update { it.copy(showApplyDialog = false, isApplying = true) }
                // Here you would call a use case to actually apply
                // For now, we simulate success
                _state.update { it.copy(isApplying = false, applySuccess = true) }
            }
            is JobOfferDetailsEvent.DismissSuccessDialog -> _state.update { it.copy(applySuccess = false) }
        }
    }

    private fun fetchDetails(uuid: String) {
        viewModelScope.launch {
            useCases.getJobOfferWorkerDetails(uuid).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> _state.update { it.copy(isLoading = true, errorMessage = "") }
                    is ApiResponse.Success -> _state.update { it.copy(isLoading = false, details = response.data) }
                    is ApiResponse.Error -> _state.update { it.copy(isLoading = false, errorMessage = response.errorMessage) }
                    is ApiResponse.Failure -> _state.update { it.copy(isLoading = false, errorMessage = response.errorMessage) }
                }
            }
        }
    }
}
