package com.example.nexum_trabajador.ui.presenter.job_offer_details

sealed class JobOfferDetailsEvent {
    data class FetchDetails(val uuid: String) : JobOfferDetailsEvent()
    object ApplyClicked : JobOfferDetailsEvent()
    object ConfirmApply : JobOfferDetailsEvent()
    object DismissDialog : JobOfferDetailsEvent()
    object DismissSuccessDialog : JobOfferDetailsEvent()
}
