package com.example.nexum_trabajador.data.market_location.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.market_location.remote.payload.res.MarketLocationRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/9/2025
 * @version 1.0
 */
@Singleton
class MarketLocationRemoteDataSource @Inject constructor(
    private val api: MarketLocationApi
) {
    fun getAllLocations(countryCode: String? = null): Flow<ApiResponse<List<MarketLocationRes>>> {
        return apiRequestFlow { api.getAllLocations(countryCode) }
    }
}