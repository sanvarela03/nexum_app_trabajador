package com.example.nexum_trabajador.ui.presenter.insurance

import android.net.Uri

sealed class InsurancesEvent {
    object Refresh : InsurancesEvent()
    object OpenAddDialog : InsurancesEvent()
    object DismissDialog : InsurancesEvent()
    data class NameChanged(val name: String) : InsurancesEvent()
    data class CoverageTypeChanged(val type: String) : InsurancesEvent()
    data class DescriptionChanged(val desc: String) : InsurancesEvent()
    data class FileSelected(val uri: Uri?) : InsurancesEvent()
    object SubmitCreate : InsurancesEvent()
    data class DeleteInsurance(val id: Long) : InsurancesEvent()
    object ClearMessages : InsurancesEvent()
}
