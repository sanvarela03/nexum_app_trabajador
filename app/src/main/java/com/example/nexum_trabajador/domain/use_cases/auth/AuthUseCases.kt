package com.example.nexum_trabajador.domain.use_cases.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class AuthUseCases @Inject constructor(
    val signIn: SignIn,
    val signUp: SignUp,
    val signOut: SignOut,
    val authenticate: Authenticate
)
