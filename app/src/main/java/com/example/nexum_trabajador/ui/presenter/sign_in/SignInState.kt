package com.example.nexum_trabajador.ui.presenter.sign_in

import com.example.nexum_trabajador.data.auth.remote.payload.res.SignInRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val signInResponse: ApiResponse<SignInRes>? = null
) {
    val isButtonEnabled: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                !emailError &&
                !passwordError &&
                signInResponse !is ApiResponse.Loading
}
