package com.example.nexum_trabajador.ui.presenter.sign_up

import android.net.Uri
import android.util.Log
import com.example.nexum_trabajador.domain.model.Country
import com.example.nexum_trabajador.domain.model.MarketLocation
import com.example.nexum_trabajador.ui.common.ValidationResult

object SignUpValidator {

    private const val MIN_EMAIL_LENGTH = 3
    private const val MAX_EMAIL_LENGTH = 254
    private const val MIN_PASSWORD_LENGTH = 5
    private const val MIN_USER_LENGTH = 4
    private const val MAX_USER_LENGTH = 20
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validateEmail(email: String?): ValidationResult {
        if (email.isNullOrBlank()) {
            return ValidationResult(false, "El correo no puede estar vacío.")
        }
        return when {
            email.length < MIN_EMAIL_LENGTH -> ValidationResult(false, "El correo es muy corto.")
            email.length > MAX_EMAIL_LENGTH -> ValidationResult(false, "El correo es muy largo.")
            !emailRegex.matches(email) -> ValidationResult(false, "Formato de correo inválido.")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String?): ValidationResult = ValidationResult(
        (!password.isNullOrEmpty() && password.length >= MIN_PASSWORD_LENGTH)
    )

    fun validateName(name: String?): ValidationResult = ValidationResult(
        !name.isNullOrEmpty() && name.length >= 4 && name.length < 20
    )

    fun validateLastName(lastName: String?): ValidationResult = ValidationResult(
        !lastName.isNullOrEmpty() && lastName.length >= 4 && lastName.length < 20
    )

    fun validatePhoneCode(country: Country?): ValidationResult = ValidationResult(
        country != null
    )

    fun validateCellphone(phone: String, country: Country?): ValidationResult {
        Log.d(
            "SignUpValidator",
            "Validating cellphone: $phone, Regex: ${country?.phoneCheckRegex}"
        )
        Log.d(
            "SignUpValidator",
            "Validating cellphone: $phone, PhoneCode: ${country?.phoneCode}"
        )
        Log.d(
            "SignUpValidator",
            "Validating cellphone IF: ${phone.isNullOrBlank() && country == null}"
        )

        if (phone.isNullOrBlank() && country == null) return ValidationResult(
            false,
            "El código y el número no pueden estar vacíos"
        )

        val digits = phone.filter { it.isDigit() }

        Log.d(
            "SignUpValidator",
            "Digits: ${digits}, Regex: ${country?.phoneCheckRegex}"
        )
        val rule = country?.phoneCheckRegex?.toRegex() ?: return ValidationResult(
            false,
            "Código de país no permitido"
        )

        Log.d("SignUpValidator", "Rule: ${rule}")
        Log.d("SignUpValidator", "Validating cellphone matches: ${!rule.matches(digits)}")
        if (!rule.matches(digits)) {
            val msg = when (country?.phoneCode) {
                "+57" -> "Colombia: debe ser móvil que empiece en 3 y tenga 10 dígitos"
                "+52" -> "México: debe tener 10 dígitos"
                "+56" -> "Chile: debe ser móvil que empiece en 9 y tenga 9 dígitos"
                else -> "Formato no válido"
            }
            return ValidationResult(false, msg)
        }
        return ValidationResult(true)
    }

    fun validateBirthDate(birthDate: String?): ValidationResult = ValidationResult(
        !birthDate.isNullOrEmpty()
    )

    fun validateCity(marketLocation: MarketLocation?): ValidationResult = ValidationResult(
        marketLocation != null
    )

    fun validateDocumentNumber(documentNumber: String?): ValidationResult {
        val isValid = !documentNumber.isNullOrEmpty() &&
                documentNumber.all { it.isDigit() } &&
                documentNumber.length in 6..10

        return ValidationResult(isValid)
    }

    fun validateDocumentUrl(documentUrl: String?): ValidationResult {
        val isValid = !documentUrl.isNullOrEmpty()
        return ValidationResult(isValid)
    }

    fun validateConfirmPassword(confirmPassword: String?, password: String?): ValidationResult =
        ValidationResult(
            confirmPassword == password
        )

    fun validateDocumentUri(documentUri: String?): ValidationResult {
        val isValid = !documentUri.isNullOrEmpty()
        return ValidationResult(isValid)
    }

    fun validateProfilePictureUri(profilePictureUri: Uri?): ValidationResult = ValidationResult(
        !profilePictureUri.toString().isEmpty()
    )

    fun validateProfilePictureUrl(profilePictureUrl: String?): ValidationResult = ValidationResult(
        !profilePictureUrl.isNullOrEmpty()
    )
}
