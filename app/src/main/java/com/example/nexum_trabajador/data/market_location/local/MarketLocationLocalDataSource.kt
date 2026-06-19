package com.example.nexum_trabajador.data.market_location.local

import com.example.nexum_trabajador.data.local_data_source.LocalDataSource
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
class MarketLocationLocalDataSource @Inject constructor(
    private val dao: MarketLocationDao
) : LocalDataSource<MarketLocationEntity> {

    override suspend fun hasResource(): Boolean {
        return getAll().isNotEmpty()
    }

    override suspend fun getAll(): List<MarketLocationEntity> {
        return dao.getAll()
    }

    override fun observe(): Flow<List<MarketLocationEntity>> {
        return dao.observe()
    }

    override suspend fun replaceAll(resources: List<MarketLocationEntity>) {
        dao.replaceAll(resources)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}