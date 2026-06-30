package com.example.nexum_trabajador.ui.presenter.sign_up

import android.net.Uri
import com.example.nexum_trabajador.domain.model.Country
import com.example.nexum_trabajador.domain.model.MarketLocation

data class SignUpState(
    val email: String = "",
    val username: String = "",
    val name: String = "",
    val lastName: String = "",
    val phoneCode: String = "",
    val phone: String = "",
    val birthDate: String = "",

    val birthDateDay: Int = 8,
    val birthDateMonth: Int = 6,
    val birthDateYear: Int = 1978,
    val documentNumber: String = "",
    val frontDocumentUri: Uri = Uri.EMPTY,
    val backDocumentUri: Uri = Uri.EMPTY,
    val frontDocumentUrl: String = "",
    val backDocumentUrl: String = "",
    val profilePictureUrl: String = "",
    val profilePictureUri: Uri = Uri.EMPTY,
    val password: String = "",
    val confirmPassword: String = "",

    val marketLocations: List<MarketLocation> = emptyList(),

    val selectedCountry: Country? = null,
    val selectedMarketLocation: MarketLocation? = null,

    val isLoadingCountries: Boolean = false,
    val countries: List<Country> = emptyList(),
    val countriesError: String? = null,

    val tosAccepted: Boolean = true,
    val policyAccepted: Boolean = true,

    val showTosDialog: Boolean = false,
    val showPolicyDialog: Boolean = false,

    val emailError: Boolean = false,
    val nameError: Boolean = false,
    val lastNameError: Boolean = false,
    val phoneCodeError: Boolean = false,
    val phoneError: Boolean = false,
    val birthDateError: Boolean = false,
    val cityError: Boolean = false,
    val documentNumberError: Boolean = false,
    val frontDocumentUrlError: Boolean = false,
    val backDocumentUrlError: Boolean = false,
    val profilePictureUrlError: Boolean = false,
    val profilePictureUriError: Boolean = false,
    val passwordError: Boolean = false,
    val confirmPasswordError: Boolean = false,

    val errorMessage: String = "",
    val frontDocumentUriError: Boolean = false,
    val backDocumentUriError: Boolean = false,

    val isSignedUp: Boolean = false,
    val signUpResponse: String = "",

    val signUpError: Boolean = false,
) {
    val isValidToContinue: Boolean
        get() = emailError.not() &&
                email.isNotEmpty()

    val isUserDataValid: Boolean
        get() = nameError.not() &&
                lastNameError.not() &&
                name.isNotEmpty() &&
                lastName.isNotEmpty()

    val isPhoneValid: Boolean
        get() = phoneError.not() &&
                phone.isNotEmpty() &&
                selectedCountry != null

    val isBirthDateValid: Boolean
        get() = birthDateError.not()

    val isCityValid: Boolean
        get() = cityError.not() && selectedMarketLocation != null

    val isIdValid: Boolean
        get() = documentNumberError.not() && documentNumber.isNotEmpty()

    val isDocumentsUploadValid: Boolean
        get() = frontDocumentUrlError.not() &&
                backDocumentUrlError.not() &&
                frontDocumentUrl.isNotEmpty() &&
                backDocumentUrl.isNotEmpty()

    val isProfilePictureValid: Boolean
        get() = profilePictureUrlError.not() && profilePictureUrl.isNotEmpty()

    val isPasswordValid: Boolean
        get() = passwordError.not() &&
                confirmPasswordError.not() &&
                password.isNotEmpty() &&
                confirmPassword.isNotEmpty() &&
                password == confirmPassword
}
