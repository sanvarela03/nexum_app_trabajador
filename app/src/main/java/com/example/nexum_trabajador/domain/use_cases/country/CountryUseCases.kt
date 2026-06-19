package com.example.nexum_trabajador.domain.use_cases.country

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class CountryUseCases @Inject constructor(
    val updateCountry: UpdateCountry,
    val observeCountry: ObserveCountry
)
