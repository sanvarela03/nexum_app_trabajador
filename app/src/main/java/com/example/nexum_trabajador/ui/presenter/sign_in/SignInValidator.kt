package com.example.nexum_trabajador.ui.presenter.sign_in

import com.example.nexum_trabajador.ui.common.ValidationResult


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/31/2025
 * @version 1.0
 */
object SignInValidator {
    fun validateEmail(email: String?): ValidationResult = ValidationResult(
        !email.isNullOrEmpty() && email.length >= 4 && email.length <= 20,
        "Email cannot be empty."
    )

    fun validatePassword(password: String?): ValidationResult = ValidationResult(
        (!password.isNullOrEmpty() && password.length >= 4)
    )
}

