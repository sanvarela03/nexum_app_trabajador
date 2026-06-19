package com.example.nexum_trabajador.ui.presenter.sign_in

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.data.auth.remote.payload.res.SignInRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.use_cases.auth.AuthUseCases
import com.example.nexum_trabajador.domain.use_cases.common.GetFcmTokenUseCase
import com.example.nexum_trabajador.domain.use_cases.common.SaveSessionUseCase
import com.example.nexum_trabajador.domain.use_cases.sign_in.GetLastUserEmailUseCase
import com.example.nexum_trabajador.domain.use_cases.sign_in.SaveLastUserEmailUseCase
import com.example.nexum_trabajador.domain.use_cases.sign_in.ValidateSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val validateSignInUseCase: ValidateSignInUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val saveLastUserEmailUseCase: SaveLastUserEmailUseCase,
    private val getFcmTokenUseCase: GetFcmTokenUseCase,
    private val getLastUserEmailUseCase: GetLastUserEmailUseCase
) : ViewModel() {

    var state by mutableStateOf(SignInState())
        private set

    init {
        loadLastUser()
    }

    private fun loadLastUser() {
        viewModelScope.launch {
            // Solo cargar si el email está vacío (no fue pasado por argumento)
            if (state.email.isEmpty()) {
                val lastEmail = getLastUserEmailUseCase()
                if (!lastEmail.isNullOrBlank()) {
                    // Actualizamos el estado y validamos para que el UI refleje el estado correcto
                    onEvent(SignInEvent.UsernameChanged(lastEmail))
                }
            }
        }
    }

    fun onEvent(event: SignInEvent) {
        state = reduceState(event)
        handleSideEffects(event)
    }

    private fun reduceState(event: SignInEvent): SignInState {
        return when (event) {
            is SignInEvent.UsernameChanged -> {
                val emailResult = validateSignInUseCase.executeEmail(event.username)
                state.copy(email = event.username, emailError = !emailResult.isValid)
            }

            is SignInEvent.PasswordChanged -> {
                val passwordResult = validateSignInUseCase.executePassword(event.password)
                state.copy(password = event.password, passwordError = !passwordResult.isValid)
            }

            SignInEvent.LoginButtonClicked -> state
            SignInEvent.ForgotPasswordButtonClicked -> state
        }
    }

    private fun handleSideEffects(event: SignInEvent) {
        when (event) {
            SignInEvent.LoginButtonClicked -> signIn()
            SignInEvent.ForgotPasswordButtonClicked -> {
                // TODO: Navegar a pantalla de recuperar contraseña
            }
            else -> {}
        }
    }

    private fun signIn() {
        // Validación final antes de enviar (por seguridad)
        val emailResult = validateSignInUseCase.executeEmail(state.email)
        val passwordResult = validateSignInUseCase.executePassword(state.password)

        if (!emailResult.isValid || !passwordResult.isValid) {
            state = state.copy(
                emailError = !emailResult.isValid,
                passwordError = !passwordResult.isValid
            )
            return
        }

        viewModelScope.launch {
            val tokenResult = getFcmTokenUseCase()
            val fcmToken = tokenResult.getOrElse {
                state = state.copy(
                    signInResponse = ApiResponse.Failure(
                        "No se pudo obtener el token del dispositivo.", -1
                    )
                )
                return@launch
            }

            authUseCases.signIn(state.email, state.password, fcmToken)
                .onStart {
                    state = state.copy(signInResponse = ApiResponse.Loading)
                }
                .catch { e ->
                    Log.e("SignInViewModel", "Sign-in flow failed", e)
                    val apiResponse =
                        ApiResponse.Failure(e.message ?: "Ocurrió un error inesperado", -1)
                    state = state.copy(signInResponse = apiResponse)
                }
                .collect { response ->
                    if (response is ApiResponse.Success) {
                        handleSuccessfulSignIn(response)
                    } else {
                        state = state.copy(signInResponse = response)
                    }
                }
        }
    }

    private suspend fun handleSuccessfulSignIn(response: ApiResponse.Success<SignInRes>) {
        val token = response.data?.token
        if (token.isNullOrBlank()) {
            state = state.copy(
                signInResponse = ApiResponse.Failure(
                    "Login exitoso pero no se recibió token.", -1
                )
            )
        } else {
            saveSessionUseCase(response.data)
            saveLastUserEmailUseCase(response.data.email)
            state = state.copy(signInResponse = response)
        }
    }
}
