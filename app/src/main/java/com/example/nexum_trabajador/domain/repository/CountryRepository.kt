package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>>
    fun observe(): Flow<List<Country>>
}
