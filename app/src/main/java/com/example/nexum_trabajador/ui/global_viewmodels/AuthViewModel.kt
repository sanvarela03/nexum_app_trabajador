package com.example.nexum_trabajador.ui.global_viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.common.UserAuthState
import com.example.nexum_trabajador.domain.use_cases.auth.AuthUseCases
import com.example.nexum_trabajador.ui.presenter.splash.SplashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(UserAuthState.UNKNOWN)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    init {
        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        viewModelScope.launch {
            authUseCases.authenticate().collect { hasToken ->
                _isAuthenticated.value = if (hasToken) {
                    UserAuthState.AUTHENTICATED
                } else {
                    UserAuthState.UNAUTHENTICATED
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AuthViewModel", "onCleared")
    }
}
