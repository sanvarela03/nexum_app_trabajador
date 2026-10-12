package com.example.nexum_trabajador.ui.presenter.sign_up

import android.net.Uri
import com.example.nexum_trabajador.domain.model.Country
import com.example.nexum_trabajador.domain.model.MarketLocation

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 06/08/2025
 * @version 1.0
 */
sealed class SignUpEvent {
    data class EmailChanged(val email: String) : SignUpEvent()
    data class NameChanged(val name: String) : SignUpEvent()
    data class LastNameChanged(val lastName: String) : SignUpEvent()
    data class PhoneCodeChanged(val country: Country?) : SignUpEvent()
    data class CellphoneChanged(val phone: String) : SignUpEvent()

    data class BirthDateDayChanged(val birthDateDay: Int) : SignUpEvent()
    data class BirthDateMonthChanged(val birthDateMonth: Int) : SignUpEvent()
    data class BirthDateYearChanged(val birthDateYear: Int) : SignUpEvent()

    data class FrontDocumentUriChanged(val frontDocumentUri: Uri) : SignUpEvent()

    data class BackDocumentUriChanged(val backDocumentUri: Uri) : SignUpEvent()

    data class FrontDocumentUrlChanged(val frontDocumentUrl: String) : SignUpEvent()

    data class BackDocumentUrlChanged(val backDocumentUrl: String) : SignUpEvent()

    data class PasswordChanged(val password: String) : SignUpEvent()

    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpEvent()
    data class ProfilePictureUriChanged(val profilePictureUri: Uri) : SignUpEvent()
    data class ProfilePictureUrlChanged(val profilePictureUrl: String) : SignUpEvent()

    data class LoadCountries(val fetchFromRemote: Boolean = false) : SignUpEvent()
    data class LoadMarketLocations(val fetchFromRemote: Boolean = false) : SignUpEvent()

    object ShowPolicyDialog : SignUpEvent()
    object ShowTosDialog : SignUpEvent()

    object DismissPolicyDialog : SignUpEvent()
    object ConfirmPolicyDialog : SignUpEvent()
    object DismissTosDialog : SignUpEvent()
    object ConfirmTosDialog : SignUpEvent()
    object DismissSignUpSuccessDialog : SignUpEvent()

    data class CityChanged(val marketLocation: MarketLocation?) : SignUpEvent()
    data class DocumentNumberChanged(val documentNumber: String) : SignUpEvent()

    object ContinueButtonClicked : SignUpEvent()
    object RegisterButtonClicked : SignUpEvent()
}
