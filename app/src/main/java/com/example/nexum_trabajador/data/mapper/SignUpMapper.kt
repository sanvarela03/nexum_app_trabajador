package com.example.nexum_trabajador.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nexum_trabajador.data.auth.remote.payload.req.ProfileReq
import com.example.nexum_trabajador.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SignUpMapper @Inject constructor() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapStateToRequest(state: SignUpState, firebaseToken: String, generatedUsername: String): SignUpReq {
        return SignUpReq(
            username = generatedUsername,
            email = state.email,
            password = state.password,
            phoneCode = state.phoneCode.split(' ').firstOrNull() ?: "",
            phoneNumber = state.phone,
            marketLocationId = state.selectedMarketLocation?.id ?: 0,
            firebaseToken = firebaseToken,
            profile = ProfileReq(
                firstName = state.name,
                lastName = state.lastName,
                birthDate = LocalDate.of(
                    state.birthDateYear,
                    state.birthDateMonth,
                    state.birthDateDay
                ).format(DateTimeFormatter.ISO_LOCAL_DATE),
                profileImageUrl = state.profilePictureUrl,
                frontDocumentUrl = state.frontDocumentUrl,
                backDocumentUrl = state.backDocumentUrl,
            )
        )
    }

    fun generateUsername(name: String, lastName: String): String {
        val firstNamePart = name.firstOrNull() ?: ""
        val lastNamePart = lastName.split(' ').firstOrNull() ?: ""
        val randomPart = (10..99).random()
        return "$firstNamePart$lastNamePart$randomPart"
    }
}
