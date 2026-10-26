package com.example.nexum_trabajador.ui.global_viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.common.UserAuthState
import com.example.nexum_trabajador.domain.use_cases.auth.AuthUseCases
import com.example.nexum_trabajador.domain.use_cases.sign_in.GetLastUserEmailUseCase
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val getLastUserEmailUseCase: GetLastUserEmailUseCase
) : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(UserAuthState.UNKNOWN)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private val _startDestinationData = MutableStateFlow<AuthRoutes>(AuthRoutes.SignUpScreen)
    val startDestinationData: StateFlow<AuthRoutes> = _startDestinationData

    init {
        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        Log.d("AuthViewModel", "observeAuthenticationState")
        viewModelScope.launch {
            authUseCases.authenticate().collect { hasToken ->
                Log.d("AuthViewModel", "hasToken: $hasToken")
                if (hasToken) {
                    _isAuthenticated.value = UserAuthState.AUTHENTICATED
                } else {
                    _isAuthenticated.value = UserAuthState.UNAUTHENTICATED
                    val email = getLastUserEmailUseCase()
                    if (!email.isNullOrEmpty()) {
                        _startDestinationData.value = AuthRoutes.SignInScreen(email)
                    } else {
                        _startDestinationData.value = AuthRoutes.SignUpScreen
                    }
                }
                Log.d("AuthViewModel", "is_Authenticated: ${_isAuthenticated.value}")
                Log.d("AuthViewModel", "isAuthenticated: ${isAuthenticated.value}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AuthViewModel", "onCleared")
    }
}
