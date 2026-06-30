package com.example.nexum_trabajador.ui.presenter.sign_up

import android.net.Uri
import android.util.Log
import com.example.nexum_trabajador.domain.model.Country
import com.example.nexum_trabajador.domain.model.MarketLocation
import com.example.nexum_trabajador.ui.common.ValidationResult

object SignUpValidator {
    fun validateEmail(email: String?): ValidationResult {
        if (email.isNullOrEmpty()) {
            return ValidationResult(false, "Email cannot be empty.")
        }

        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")


        val isValid = emailRegex.matches(email) && email.length >= 4

        val errorMessage = when {
            !emailRegex.matches(email) -> "Invalid email format."
            email.length < 4 -> "Email is too short (minimum 4 characters)."
            else -> ""
        }

        return ValidationResult(isValid, errorMessage)
    }

    fun validatePassword(password: String?): ValidationResult = ValidationResult(
        (!password.isNullOrEmpty() && password.length >= 4)
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
            "El el codigo y el numero no pueden estar vacios"
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
        Log.d("SignUpValidator", "Validating cellphone matchces: ${!rule.matches(digits)}")
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

    fun validateConfirmPassword(confirmPassword: String?, password: String?): ValidationResult = ValidationResult(
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
