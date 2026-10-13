package com.example.nexum_trabajador.ui.presenter.home

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import com.example.nexum_trabajador.domain.model.Worker

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/1/2025
 * @version 1.0
 */
data class HomeState(
    val worker: Worker? = null,
    var customerInfo: String = "",
    val isRefreshing: Boolean = false,
    var errorMessage: String = "",
    val isSignOutLoading: Boolean = false,
    val signOutResponse: MessageRes? = null,
    val showDialog: Boolean = false,
    val dialogTitle: String = "",
    val dialogMessage: String = "",
    val isSuccess: Boolean = false,
    val isUpdating: Boolean = false,
    val isTogglingAvailability: Boolean = false,
    val occupations: List<JobOccupationRes> = emptyList(),
    val isLoadingOccupations: Boolean = false
)