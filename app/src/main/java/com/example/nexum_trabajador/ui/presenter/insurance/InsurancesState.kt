package com.example.nexum_trabajador.ui.presenter.insurance

import android.net.Uri
import com.example.nexum_trabajador.data.insurance.remote.payload.res.InsuranceRes

data class InsurancesState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isUploadingFile: Boolean = false,
    val insurances: List<InsuranceRes> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    // Add dialog state
    val showAddDialog: Boolean = false,
    val insuranceName: String = "",
    val coverageType: String = "",
    val description: String = "",
    val selectedFileUri: Uri? = null,
    val uploadedFileUrl: String = ""
)
