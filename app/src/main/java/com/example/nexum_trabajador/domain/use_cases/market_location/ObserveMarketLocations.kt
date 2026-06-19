package com.example.nexum_trabajador.domain.use_cases.market_location

import com.example.nexum_trabajador.domain.repository.MarketLocationRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/11/2025
 * @version 1.0
 */
class ObserveMarketLocations @Inject constructor(
    private val repository: MarketLocationRepository
) {
    operator fun invoke() = repository.observe()
}