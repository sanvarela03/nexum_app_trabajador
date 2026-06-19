package com.example.nexum_trabajador.data.market_location

import com.example.nexum_trabajador.common.updateResourceFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.market_location.local.MarketLocationLocalDataSource
import com.example.nexum_trabajador.data.market_location.mapper.MarketLocationMapper
import com.example.nexum_trabajador.data.market_location.remote.MarketLocationRemoteDataSource
import com.example.nexum_trabajador.di.modules.IoDispatcher
import com.example.nexum_trabajador.domain.model.MarketLocation
import com.example.nexum_trabajador.domain.repository.MarketLocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
class MarketLocationRepositoryImpl @Inject constructor(
    private val localDataSource: MarketLocationLocalDataSource,
    private val remoteDataSource: MarketLocationRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : MarketLocationRepository {
    override fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> {
        return updateResourceFlow(
            fetchFromRemote = fetchFromRemote,
            checkCache = { localDataSource.hasResource() },
            remoteCall = { remoteDataSource.getAllLocations() },
            saveToCache = { localDataSource.replaceAll(MarketLocationMapper.toEntity(it)) },
            clearCache = { localDataSource.clearAll() },
            dispatcher = dispatcher
        )
    }

    override fun observe(): Flow<List<MarketLocation>> =
        localDataSource.observe().map { MarketLocationMapper.toDomain(it) }

}