package com.example.nexum_trabajador.domain.use_cases.market_location

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class MarketLocationUseCases @Inject constructor(
    val updateMarketLocations: UpdateMarketLocations,
    val observeMarketLocations: ObserveMarketLocations
)
