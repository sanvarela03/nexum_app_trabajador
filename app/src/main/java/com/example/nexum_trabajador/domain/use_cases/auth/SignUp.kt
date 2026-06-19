package com.example.nexum_trabajador.domain.use_cases.auth

import com.example.nexum_trabajador.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_trabajador.domain.repository.AuthRepository
import javax.inject.Inject

class SignUp @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(signUpReq: SignUpReq) =
        authRepository.signUp(signUpReq)
}
