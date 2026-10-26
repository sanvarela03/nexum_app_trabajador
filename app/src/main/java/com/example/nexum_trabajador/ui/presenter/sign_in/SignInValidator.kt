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
    private const val MIN_EMAIL_LENGTH = 3
    private const val MAX_EMAIL_LENGTH = 254
    private const val MIN_PASSWORD_LENGTH = 5
    private const val MIN_USER_LENGTH = 4
    private const val MAX_USER_LENGTH = 20
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val usernameRegex = Regex("^[A-Za-z0-9._]+$")

    fun validateEmailOrUsername(input: String?): ValidationResult {
        if (input.isNullOrBlank()) {
            return ValidationResult(false, "El correo o usuario no puede estar vacío.")
        }

        val isEmail = input.contains("@") && input.substringAfter("@").contains(".")

        return if (isEmail) {
            when {
                input.length < MIN_EMAIL_LENGTH -> ValidationResult(false, "El correo es muy corto.")
                input.length > MAX_EMAIL_LENGTH -> ValidationResult(false, "El correo es muy largo.")
                !emailRegex.matches(input) -> ValidationResult(false, "Formato de correo inválido.")
                else -> ValidationResult(true)
            }
        } else {
            when {
                !usernameRegex.matches(input) -> ValidationResult(false, "Formato de nombre de usuario inválido.")
                input.length < MIN_USER_LENGTH -> ValidationResult(
                    false,
                    "Usuario muy corto (mínimo $MIN_USER_LENGTH caracteres)."
                )
                input.length > MAX_USER_LENGTH -> ValidationResult(
                    false,
                    "Usuario muy largo (máximo $MAX_USER_LENGTH caracteres)."
                )
                else -> ValidationResult(true)
            }
        }
    }

    fun validateEmail(email: String?): ValidationResult = validateEmailOrUsername(email)

    fun validatePassword(password: String?): ValidationResult {
        if (password.isNullOrBlank()) {
            return ValidationResult(false, "La contraseña no puede estar vacía.")
        }
        return if (password.length >= MIN_PASSWORD_LENGTH) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "La contraseña debe tener al menos $MIN_PASSWORD_LENGTH caracteres.")
        }
    }
}
