package com.example.nexum_trabajador.domain.use_cases.job_offer

import javax.inject.Inject

data class JobOfferUseCases @Inject constructor(
    val getJobOfferWorkerDetails: GetJobOfferWorkerDetails
)
