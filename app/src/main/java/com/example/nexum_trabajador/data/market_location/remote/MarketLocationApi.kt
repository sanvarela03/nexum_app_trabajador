package com.example.nexum_trabajador.data.market_location.remote

import com.example.nexum_trabajador.data.market_location.remote.payload.res.MarketLocationRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/9/2025
 * @version 1.0
 */
interface MarketLocationApi {
    @GET("api/v1/market-locations")
    suspend fun getAllLocations(
        @Query("country_code") countryCode: String? = null
    ): Response<List<MarketLocationRes>>
}