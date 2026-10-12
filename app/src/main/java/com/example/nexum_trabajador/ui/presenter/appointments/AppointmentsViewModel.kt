package com.example.nexum_trabajador.ui.presenter.appointments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.use_cases.contract.ContractUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val contractUseCases: ContractUseCases
) : ViewModel() {

    var state by mutableStateOf(AppointmentsState())
        private set

    init {
        loadContracts(isRefresh = false)
    }

    fun onEvent(event: AppointmentsEvent) {
        when (event) {
            AppointmentsEvent.LoadContracts -> loadContracts(isRefresh = false)
            AppointmentsEvent.RefreshContracts -> loadContracts(isRefresh = true)
            is AppointmentsEvent.SelectFilter -> state = state.copy(selectedFilter = event.filter)
            is AppointmentsEvent.OpenVerificationDialog -> {
                state = state.copy(
                    selectedContract = event.contract,
                    verificationCodeInput = "",
                    showVerificationDialog = true,
                    errorMessage = null
                )
            }
            AppointmentsEvent.DismissVerificationDialog -> {
                state = state.copy(showVerificationDialog = false, selectedContract = null, verificationCodeInput = "")
            }
            is AppointmentsEvent.VerificationCodeChanged -> {
                if (event.code.length <= 6 && event.code.all { it.isDigit() }) {
                    state = state.copy(verificationCodeInput = event.code)
                }
            }
            AppointmentsEvent.SubmitVerificationCode -> verifyMeeting()
            is AppointmentsEvent.OpenInvoiceDialog -> {
                val defaultAmount = event.contract.agreedAmount?.let {
                    if (it % 1.0 == 0.0) it.toLong().toString() else it.toString()
                } ?: ""
                state = state.copy(
                    selectedContract = event.contract,
                    invoiceAmountInput = defaultAmount,
                    invoiceNotesInput = "Mano de obra conforme a las especificaciones del servicio.",
                    showInvoiceDialog = true,
                    errorMessage = null
                )
            }
            AppointmentsEvent.DismissInvoiceDialog -> {
                state = state.copy(showInvoiceDialog = false, selectedContract = null)
            }
            is AppointmentsEvent.InvoiceAmountChanged -> {
                state = state.copy(invoiceAmountInput = event.amount.filter { it.isDigit() || it == '.' })
            }
            is AppointmentsEvent.InvoiceNotesChanged -> {
                state = state.copy(invoiceNotesInput = event.notes)
            }
            AppointmentsEvent.SubmitInvoice -> generateInvoice()
            is AppointmentsEvent.OpenCompletionDialog -> {
                state = state.copy(
                    selectedContract = event.contract,
                    showCompletionDialog = true,
                    errorMessage = null
                )
            }
            AppointmentsEvent.DismissCompletionDialog -> {
                state = state.copy(showCompletionDialog = false, selectedContract = null)
            }
            AppointmentsEvent.ConfirmWorkCompleted -> completeWork()
            AppointmentsEvent.ClearMessages -> state = state.copy(errorMessage = null, successMessage = null)
        }
    }

    private fun loadContracts(isRefresh: Boolean) {
        viewModelScope.launch {
            contractUseCases.getWorkerContracts().catch { e ->
                state = state.copy(
                    isLoading = false,
                    isRefreshing = false,
                    errorMessage = e.message ?: "Error al cargar citas y contratos"
                )
            }.collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        state = if (isRefresh) state.copy(isRefreshing = true) else state.copy(isLoading = true)
                    }
                    is ApiResponse.Success -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            contracts = response.data
                        )
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = response.errorMessage
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = response.errorMessage
                        )
                    }
                }
            }
        }
    }

    private fun verifyMeeting() {
        val contractId = state.selectedContract?.id ?: return
        val code = state.verificationCodeInput.trim()

        if (code.length != 6) {
            state = state.copy(errorMessage = "El código debe tener 6 dígitos numéricos")
            return
        }

        viewModelScope.launch {
            contractUseCases.verifyMeeting(contractId, code).catch { e ->
                state = state.copy(
                    isVerifying = false,
                    errorMessage = e.message ?: "Error al verificar código"
                )
            }.collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        state = state.copy(isVerifying = true, errorMessage = null)
                    }
                    is ApiResponse.Success -> {
                        val updatedContract = response.data
                        val updatedList = state.contracts.map {
                            if (it.id == updatedContract.id) updatedContract else it
                        }
                        state = state.copy(
                            isVerifying = false,
                            showVerificationDialog = false,
                            selectedContract = null,
                            verificationCodeInput = "",
                            contracts = updatedList,
                            successMessage = "¡Encuentro verificado con éxito! Ahora puedes iniciar las labores acordadas."
                        )
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(
                            isVerifying = false,
                            errorMessage = response.errorMessage
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(
                            isVerifying = false,
                            errorMessage = response.errorMessage
                        )
                    }
                }
            }
        }
    }

    private fun generateInvoice() {
        val contractId = state.selectedContract?.id ?: return
        val amount = state.invoiceAmountInput.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            state = state.copy(errorMessage = "Ingresa un monto válido para la factura de mano de obra")
            return
        }
        val notes = state.invoiceNotesInput.trim().ifEmpty { null }

        viewModelScope.launch {
            contractUseCases.generateInvoice(contractId, amount, notes).catch { e ->
                state = state.copy(
                    isGeneratingInvoice = false,
                    errorMessage = e.message ?: "Error al emitir factura"
                )
            }.collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        state = state.copy(isGeneratingInvoice = true, errorMessage = null)
                    }
                    is ApiResponse.Success -> {
                        val updatedContract = response.data
                        val updatedList = state.contracts.map {
                            if (it.id == updatedContract.id) updatedContract else it
                        }
                        state = state.copy(
                            isGeneratingInvoice = false,
                            showInvoiceDialog = false,
                            selectedContract = null,
                            contracts = updatedList,
                            successMessage = "¡Factura emitida con éxito! Notificamos al cliente para que realice el pago de la mano de obra."
                        )
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(
                            isGeneratingInvoice = false,
                            errorMessage = response.errorMessage
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(
                            isGeneratingInvoice = false,
                            errorMessage = response.errorMessage
                        )
                    }
                }
            }
        }
    }

    private fun completeWork() {
        val contractId = state.selectedContract?.id ?: return

        viewModelScope.launch {
            contractUseCases.completeWorkByWorker(contractId).catch { e ->
                state = state.copy(
                    isCompleting = false,
                    errorMessage = e.message ?: "Error al marcar trabajo terminado"
                )
            }.collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        state = state.copy(isCompleting = true, errorMessage = null)
                    }
                    is ApiResponse.Success -> {
                        val updatedContract = response.data
                        val updatedList = state.contracts.map {
                            if (it.id == updatedContract.id) updatedContract else it
                        }
                        state = state.copy(
                            isCompleting = false,
                            showCompletionDialog = false,
                            selectedContract = null,
                            contracts = updatedList,
                            successMessage = "¡Trabajo marcado como finalizado! El cliente revisará y liberará el pago."
                        )
                    }
                    is ApiResponse.Failure -> {
                        state = state.copy(
                            isCompleting = false,
                            errorMessage = response.errorMessage
                        )
                    }
                    is ApiResponse.Error -> {
                        state = state.copy(
                            isCompleting = false,
                            errorMessage = response.errorMessage
                        )
                    }
                }
            }
        }
    }
}
