package com.example.nexum_trabajador.data.country.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.country.remote.payload.res.CountryRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
@Singleton
class CountryRemoteDataSource @Inject constructor(
    private val api: CountryApi
) {
    fun getCountries(): Flow<ApiResponse<List<CountryRes>>> {
        return apiRequestFlow { api.getCountries() }
    }
}