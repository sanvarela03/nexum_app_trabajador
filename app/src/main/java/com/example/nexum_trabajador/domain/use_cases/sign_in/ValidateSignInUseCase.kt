package com.example.nexum_trabajador.domain.use_cases.sign_in

import com.example.nexum_trabajador.ui.common.ValidationResult
import com.example.nexum_trabajador.ui.presenter.sign_in.SignInValidator
import javax.inject.Inject

class ValidateSignInUseCase @Inject constructor() {
    fun executeEmail(email: String): ValidationResult {
        return SignInValidator.validateEmail(email)
    }

    fun executePassword(password: String): ValidationResult {
        return SignInValidator.validatePassword(password)
    }
}
