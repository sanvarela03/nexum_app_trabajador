package com.example.nexum_trabajador.ui.presenter.sign_in

import com.example.nexum_trabajador.data.auth.remote.payload.res.SignInRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val signInResponse: ApiResponse<SignInRes>? = null,
    val isLoading: Boolean = false,
    var errorMessage: String = "",
    val isResponseError: Boolean = false
) {
    val isButtonEnabled: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                emailError.not() &&
                passwordError.not() &&
                isLoading.not()
}
