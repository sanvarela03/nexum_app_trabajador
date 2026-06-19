package com.example.nexum_trabajador.domain.use_cases.auth

import com.example.nexum_trabajador.data.auth.remote.payload.req.SignInReq
import com.example.nexum_trabajador.domain.repository.AuthRepository
import javax.inject.Inject

class SignIn @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String, firebaseToken: String) =
        repository.signIn(SignInReq(email, password, firebaseToken))
}
