package com.example.nexum_trabajador.ui.presenter.sign_up

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.mapper.SignUpMapper
import com.example.nexum_trabajador.domain.use_cases.auth.AuthUseCases
import com.example.nexum_trabajador.domain.use_cases.common.GetFcmTokenUseCase
import com.example.nexum_trabajador.domain.use_cases.common.MediaManagementUseCase
import com.example.nexum_trabajador.domain.use_cases.country.CountryUseCases
import com.example.nexum_trabajador.domain.use_cases.market_location.MarketLocationUseCases
import com.example.nexum_trabajador.domain.use_cases.sign_up.RestoreSignUpDraftUseCase
import com.example.nexum_trabajador.domain.use_cases.sign_up.ValidateSignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val countryUseCases: CountryUseCases,
    private val marketLocationUseCases: MarketLocationUseCases,
    private val mediaManagementUseCase: MediaManagementUseCase,
    private val restoreSignUpDraftUseCase: RestoreSignUpDraftUseCase,
    private val getFcmTokenUseCase: GetFcmTokenUseCase,
    private val validateSignUpUseCase: ValidateSignUpUseCase,
    private val signUpMapper: SignUpMapper
) : ViewModel() {

    var state by mutableStateOf(SignUpState())
        private set

    private var signUpJob: Job? = null

    init {
        loadPersistedUrls()
        refreshCountries(true)
        observeCountries()
        observeMarketLocations()
        refreshMarketLocations(true)
    }

    private fun loadPersistedUrls() {
        viewModelScope.launch {
            val draft = restoreSignUpDraftUseCase()
            state = state.copy(
                frontDocumentUrl = draft.frontDocumentUrl,
                backDocumentUrl = draft.backDocumentUrl,
                profilePictureUrl = draft.profilePictureUrl
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: SignUpEvent) {
        // Reducer: Actualiza el estado de forma atómica
        state = reduceState(event)

        // Side Effects: Maneja lógica asíncrona o de navegación
        handleEventSideEffects(event)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun reduceState(event: SignUpEvent): SignUpState {
        return when (event) {
            is SignUpEvent.EmailChanged -> {
                val result = validateSignUpUseCase.executeEmail(event.email)
                state.copy(email = event.email, emailError = !result.isValid)
            }

            is SignUpEvent.NameChanged -> {
                val result = validateSignUpUseCase.executeName(event.name)
                state.copy(name = event.name, nameError = !result.isValid)
            }

            is SignUpEvent.LastNameChanged -> {
                val result = validateSignUpUseCase.executeLastName(event.lastName)
                state.copy(lastName = event.lastName, lastNameError = !result.isValid)
            }

            is SignUpEvent.PhoneCodeChanged -> {
                val phoneCodeResult = validateSignUpUseCase.executePhoneCode(event.country)
                val phoneResult = validateSignUpUseCase.executeCellphone(state.phone, event.country)
                state.copy(
                    selectedCountry = event.country,
                    phoneCodeError = !phoneCodeResult.isValid,
                    phoneError = !phoneResult.isValid
                )
            }

            is SignUpEvent.CellphoneChanged -> {
                val result =
                    validateSignUpUseCase.executeCellphone(event.phone, state.selectedCountry)
                state.copy(phone = event.phone, phoneError = !result.isValid)
            }

            is SignUpEvent.BirthDateDayChanged -> {
                val result = validateSignUpUseCase.executeBirthDate(
                    event.birthDateDay,
                    state.birthDateMonth,
                    state.birthDateYear
                )
                state.copy(birthDateDay = event.birthDateDay, birthDateError = !result.isValid)
            }

            is SignUpEvent.BirthDateMonthChanged -> {
                val result = validateSignUpUseCase.executeBirthDate(
                    state.birthDateDay,
                    event.birthDateMonth,
                    state.birthDateYear
                )
                state.copy(birthDateMonth = event.birthDateMonth, birthDateError = !result.isValid)
            }

            is SignUpEvent.BirthDateYearChanged -> {
                val result = validateSignUpUseCase.executeBirthDate(
                    state.birthDateDay,
                    state.birthDateMonth,
                    event.birthDateYear
                )
                state.copy(birthDateYear = event.birthDateYear, birthDateError = !result.isValid)
            }

            is SignUpEvent.CityChanged -> {
                val result = validateSignUpUseCase.executeCity(event.marketLocation)
                state.copy(
                    selectedMarketLocation = event.marketLocation,
                    cityError = !result.isValid
                )
            }

            is SignUpEvent.DocumentNumberChanged -> {
                val result = validateSignUpUseCase.executeDocumentNumber(event.documentNumber)
                state.copy(
                    documentNumber = event.documentNumber,
                    documentNumberError = !result.isValid
                )
            }

            is SignUpEvent.PasswordChanged -> {
                val passwordResult = validateSignUpUseCase.executePassword(event.password)
                val confirmPasswordResult = validateSignUpUseCase.executeConfirmPassword(
                    event.password,
                    state.confirmPassword
                )
                state.copy(
                    password = event.password,
                    passwordError = !passwordResult.isValid,
                    confirmPasswordError = !confirmPasswordResult.isValid
                )
            }

            is SignUpEvent.ConfirmPasswordChanged -> {
                val result = validateSignUpUseCase.executeConfirmPassword(
                    state.password,
                    event.confirmPassword
                )
                state.copy(
                    confirmPassword = event.confirmPassword,
                    confirmPasswordError = !result.isValid
                )
            }

            // Eventos que no requieren validación en este punto, solo actualizan el estado
            is SignUpEvent.BackDocumentUriChanged -> state.copy(backDocumentUri = event.backDocumentUri)
            is SignUpEvent.FrontDocumentUriChanged -> state.copy(frontDocumentUri = event.frontDocumentUri)
            is SignUpEvent.ProfilePictureUriChanged -> state.copy(profilePictureUri = event.profilePictureUri)
            is SignUpEvent.LoadCountries -> state.copy(isLoadingCountries = true)

            // Estos eventos son resultados de side effects, ahora validan al actualizar
            is SignUpEvent.FrontDocumentUrlChanged -> {
                val result = validateSignUpUseCase.executeDocumentUrl(event.frontDocumentUrl)
                state.copy(
                    frontDocumentUrl = event.frontDocumentUrl,
                    frontDocumentUrlError = !result.isValid
                )
            }

            is SignUpEvent.BackDocumentUrlChanged -> {
                val result = validateSignUpUseCase.executeDocumentUrl(event.backDocumentUrl)
                state.copy(
                    backDocumentUrl = event.backDocumentUrl,
                    backDocumentUrlError = !result.isValid
                )
            }

            is SignUpEvent.ProfilePictureUrlChanged -> {
                val result = validateSignUpUseCase.executeProfilePictureUrl(event.profilePictureUrl)
                state.copy(
                    profilePictureUrl = event.profilePictureUrl,
                    profilePictureUrlError = !result.isValid
                )
            }

            // Eventos de UI/Diálogos
            SignUpEvent.ConfirmTosDialog -> state.copy(showTosDialog = false, tosAccepted = true)
            SignUpEvent.DismissTosDialog -> state.copy(showTosDialog = false, tosAccepted = false)
            SignUpEvent.ConfirmPolicyDialog -> state.copy(
                showPolicyDialog = false,
                policyAccepted = true
            )

            SignUpEvent.DismissPolicyDialog -> state.copy(
                showPolicyDialog = false,
                policyAccepted = false
            )

            SignUpEvent.DismissSignUpSuccessDialog -> state.copy(isSignedUp = false)
            SignUpEvent.ShowPolicyDialog -> state.copy(showPolicyDialog = true)
            SignUpEvent.ShowTosDialog -> state.copy(showTosDialog = true)

            // Eventos que solo disparan side-effects
            SignUpEvent.ContinueButtonClicked, SignUpEvent.RegisterButtonClicked -> state
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleEventSideEffects(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.FrontDocumentUriChanged -> {
                handleMediaChange(
                    newUri = event.frontDocumentUri,
                    currentUrl = state.frontDocumentUrl,
                    storageKey = "frontDocumentUrl",
                    onSuccess = { onEvent(SignUpEvent.FrontDocumentUrlChanged(it)) },
                    onDelete = { onEvent(SignUpEvent.FrontDocumentUrlChanged("")) }
                )
            }

            is SignUpEvent.BackDocumentUriChanged -> {
                handleMediaChange(
                    newUri = event.backDocumentUri,
                    currentUrl = state.backDocumentUrl,
                    storageKey = "backDocumentUrl",
                    onSuccess = { onEvent(SignUpEvent.BackDocumentUrlChanged(it)) },
                    onDelete = { onEvent(SignUpEvent.BackDocumentUrlChanged("")) }
                )
            }

            is SignUpEvent.ProfilePictureUriChanged -> {
                handleMediaChange(
                    newUri = event.profilePictureUri,
                    currentUrl = state.profilePictureUrl,
                    storageKey = "profilePictureUrl",
                    onSuccess = { onEvent(SignUpEvent.ProfilePictureUrlChanged(it)) },
                    onDelete = { onEvent(SignUpEvent.ProfilePictureUrlChanged("")) }
                )
            }

            SignUpEvent.RegisterButtonClicked -> signUp()
            is SignUpEvent.LoadCountries -> refreshCountries(event.fetchFromRemote)
            else -> { /* Sin efectos secundarios para otros eventos */
            }
        }
    }

    private fun handleMediaChange(
        newUri: Uri?,
        currentUrl: String,
        storageKey: String,
        onSuccess: (String) -> Unit,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            if (newUri == null || newUri == Uri.EMPTY) {
                mediaManagementUseCase.deleteImage(currentUrl, storageKey)
                    .onSuccess { onDelete() }
                    .onFailure { Log.e("SignUpViewModel", "Error deleting image", it) }
            } else {
                mediaManagementUseCase.uploadAndSaveImage(newUri, currentUrl, storageKey)
                    .onSuccess(onSuccess)
                    .onFailure { Log.e("SignUpViewModel", "Error uploading image", it) }
            }
        }
    }

    private fun refreshCountries(fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            countryUseCases.updateCountry(fetchFromRemote)
                .onStart { state = state.copy(isLoadingCountries = true) }
                .catch { exception -> handleNetworkError(exception) }
                .collect { result ->
                    when (result) {
                        is ApiResponse.Success -> state = state.copy(isLoadingCountries = false)
                        is ApiResponse.Error -> state = state.copy(
                            isLoadingCountries = false,
                            countriesError = result.errorMessage
                        )

                        is ApiResponse.Failure -> state = state.copy(
                            isLoadingCountries = false,
                            countriesError = result.errorMessage
                        )

                        is ApiResponse.Loading -> state = state.copy(isLoadingCountries = true)
                    }
                }
        }
    }

    private fun observeCountries() {
        viewModelScope.launch {
            countryUseCases.observeCountry()
                .distinctUntilChanged()
                .catch { handleNetworkError(it) }
                .collect { countries ->
                    state = state.copy(isLoadingCountries = false, countries = countries)
                }
        }
    }

    private fun handleNetworkError(exception: Throwable) {
        val errorMessage = when (exception) {
            is SocketTimeoutException -> "Revisa tu conexión a internet."
            is UnknownHostException -> "No se pudo conectar al servidor."
            else -> "Ocurrió un error: ${exception.message}"
        }
        state = state.copy(isLoadingCountries = false, countriesError = errorMessage)
        Log.e("SignUpViewModel", "Network Error", exception)
    }

    private fun refreshMarketLocations(fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            marketLocationUseCases.updateMarketLocations(fetchFromRemote).catch {
                Log.d("SignUpViewModel", "updateMarketLocations Error: $it");
            }.collect {
                Log.d("SignUpViewModel", "updateMarketLocations: $it");
            }
        }
    }

    private fun observeMarketLocations() {
        viewModelScope.launch {
            marketLocationUseCases.observeMarketLocations()
                .distinctUntilChanged()
                .catch { Log.e("SignUpViewModel", "observeMarketLocations Error: $it"); }
                .collect { state = state.copy(marketLocations = it) }
        }
    }

    fun resetSignUpGlobalValidation() {
        state = state.copy(
            signUpError = false,
            errorMessage = ""
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun signUp() {
        signUpJob?.cancel()
        signUpJob = viewModelScope.launch {
            val tokenResult = getFcmTokenUseCase()
            val tkn = tokenResult.getOrElse {
                state = state.copy(
                    signUpError = true,
                    errorMessage = "No se pudo obtener el token de notificación."
                )
                return@launch
            }

            val username = signUpMapper.generateUsername(state.name, state.lastName)
            state = state.copy(username = username)

            val signUpReq = signUpMapper.mapStateToRequest(state, tkn, username)

            Log.d("SignUpRequest", signUpReq.toString())
            authUseCases.signUp(signUpReq).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        state =
                            state.copy(isSignedUp = true, signUpResponse = response.data.message)
                    }

                    is ApiResponse.Error -> {
                        state = state.copy(signUpError = true, errorMessage = response.errorMessage)
                    }

                    is ApiResponse.Failure -> {
                        state = state.copy(signUpError = true, errorMessage = response.errorMessage)
                    }

                    is ApiResponse.Loading -> { /* Opcional: mostrar un spinner global */
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        signUpJob?.cancel()
        Log.d("Cleared", "onCleared")
    }
}
