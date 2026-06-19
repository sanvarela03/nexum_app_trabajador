package com.example.nexum_trabajador.data.country

import com.example.nexum_trabajador.common.updateResourceFlow
import com.example.nexum_trabajador.data.country.local.CountryLocalDataSource
import com.example.nexum_trabajador.data.country.mapper.CountryMapper
import com.example.nexum_trabajador.data.country.remote.CountryRemoteDataSource
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.di.modules.IoDispatcher
import com.example.nexum_trabajador.domain.model.Country
import com.example.nexum_trabajador.domain.repository.CountryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
class CountryRepositoryImpl @Inject constructor(
    private val localDataSource: CountryLocalDataSource,
    private val remoteDataSource: CountryRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : CountryRepository {
    override fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> {
        return updateResourceFlow(
            fetchFromRemote = fetchFromRemote,
            checkCache = { localDataSource.hasResource() },
            remoteCall = { remoteDataSource.getCountries() },
            saveToCache = { localDataSource.replaceAll(CountryMapper.toEntity(it)) },
            clearCache = { localDataSource.clearAll() },
            dispatcher = dispatcher
        )
    }

    override fun observe(): Flow<List<Country>> = localDataSource.observe().map {
        CountryMapper.toDomain(it)
    }

}
