package com.example.nexum_trabajador.data.country.remote

import com.example.nexum_trabajador.common.APP_KEY
import com.example.nexum_trabajador.data.country.remote.payload.res.CountryRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
interface CountryApi {
    @Headers("X-API-Key: $APP_KEY")
    @GET("/api/v1/countries")
    suspend fun getCountries(): Response<List<CountryRes>>
}