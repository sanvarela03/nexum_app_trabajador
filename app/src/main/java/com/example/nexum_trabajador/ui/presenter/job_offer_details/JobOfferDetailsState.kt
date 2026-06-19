package com.example.nexum_trabajador.ui.presenter.job_offer_details

import com.example.nexum_trabajador.data.job_offer.remote.payload.res.JobOfferWorkerDetailsDto

data class JobOfferDetailsState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val details: JobOfferWorkerDetailsDto? = null,
    val showApplyDialog: Boolean = false,
    val isApplying: Boolean = false,
    val applySuccess: Boolean = false
)
