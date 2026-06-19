package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.model.MarketLocation
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/11/2025
 * @version 1.0
 */
interface MarketLocationRepository {
    fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>>
    fun observe() : Flow<List<MarketLocation>>
}