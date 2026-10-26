package com.example.nexum_trabajador.ui.presenter.appointments

import com.example.nexum_trabajador.data.contract.remote.payload.res.ContractRes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
data class AppointmentsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val contracts: List<ContractRes> = emptyList(),
    val selectedFilter: String = "ALL", // "ALL", "SCHEDULED", "VERIFIED", "COMPLETED"
    val selectedContract: ContractRes? = null,
    val verificationCodeInput: String = "",
    val isVerifying: Boolean = false,
    val isCompleting: Boolean = false,
    val isGeneratingInvoice: Boolean = false,
    val showVerificationDialog: Boolean = false,
    val showInvoiceDialog: Boolean = false,
    val showCompletionDialog: Boolean = false,
    val invoiceAmountInput: String = "",
    val invoiceNotesInput: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null
)
