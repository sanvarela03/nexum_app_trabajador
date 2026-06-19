package com.example.nexum_trabajador.domain.use_cases.sign_up

import android.net.Uri
import com.example.nexum_trabajador.domain.model.Country
import com.example.nexum_trabajador.domain.model.MarketLocation
import com.example.nexum_trabajador.ui.common.ValidationResult
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpValidator
//import com.example.nexum_trabajador.ui.presenter.sign_up.ValidationResult
import javax.inject.Inject

/**
 * Caso de uso que encapsula todas las validaciones del formulario de registro.
 * Esto permite inyectar la lógica de validación y facilita el testing del ViewModel.
 */
class ValidateSignUpUseCase @Inject constructor() {

    // Podríamos mover la lógica de SignUpValidator aquí adentro, 
    // pero por ahora actuaremos como un Facade para no romper otros usos.

    fun executeEmail(email: String): ValidationResult {
        return SignUpValidator.validateEmail(email)
    }

    fun executeName(name: String): ValidationResult {
        return SignUpValidator.validateName(name)
    }

    fun executeLastName(lastName: String): ValidationResult {
        return SignUpValidator.validateLastName(lastName)
    }

    fun executePhoneCode(country: Country?): ValidationResult {
        return SignUpValidator.validatePhoneCode(country)
    }

    fun executeCellphone(phone: String, country: Country?): ValidationResult {
        return SignUpValidator.validateCellphone(phone, country)
    }

    fun executeBirthDate(day: Int, month: Int, year: Int): ValidationResult {
        return SignUpValidator.validateBirthDate("$day-$month-$year")
    }

    fun executeCity(marketLocation: MarketLocation?): ValidationResult {
        return SignUpValidator.validateCity(marketLocation)
    }

    fun executeDocumentNumber(documentNumber: String): ValidationResult {
        return SignUpValidator.validateDocumentNumber(documentNumber)
    }

    fun executeDocumentUri(uri: String): ValidationResult {
        return SignUpValidator.validateDocumentUri(uri)
    }

    fun executeDocumentUrl(url: String): ValidationResult {
        return SignUpValidator.validateDocumentUrl(url)
    }

    fun executeProfilePictureUri(uri: Uri): ValidationResult {
        return SignUpValidator.validateProfilePictureUri(uri)
    }

    fun executeProfilePictureUrl(url: String): ValidationResult {
        return SignUpValidator.validateProfilePictureUrl(url)
    }

    fun executePassword(password: String): ValidationResult {
        return SignUpValidator.validatePassword(password)
    }

    fun executeConfirmPassword(password: String, confirm: String): ValidationResult {
        return SignUpValidator.validateConfirmPassword(confirm, password)
    }
}
