package com.example.nexum_trabajador.ui.presenter.appointments

import com.example.nexum_trabajador.data.contract.remote.payload.res.ContractRes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
sealed class AppointmentsEvent {
    object LoadContracts : AppointmentsEvent()
    object RefreshContracts : AppointmentsEvent()
    data class SelectFilter(val filter: String) : AppointmentsEvent()
    data class OpenVerificationDialog(val contract: ContractRes) : AppointmentsEvent()
    object DismissVerificationDialog : AppointmentsEvent()
    data class VerificationCodeChanged(val code: String) : AppointmentsEvent()
    object SubmitVerificationCode : AppointmentsEvent()
    data class OpenInvoiceDialog(val contract: ContractRes) : AppointmentsEvent()
    object DismissInvoiceDialog : AppointmentsEvent()
    data class InvoiceAmountChanged(val amount: String) : AppointmentsEvent()
    data class InvoiceNotesChanged(val notes: String) : AppointmentsEvent()
    object SubmitInvoice : AppointmentsEvent()
    data class OpenCompletionDialog(val contract: ContractRes) : AppointmentsEvent()
    object DismissCompletionDialog : AppointmentsEvent()
    object ConfirmWorkCompleted : AppointmentsEvent()
    object ClearMessages : AppointmentsEvent()
}
