package com.example.nexum_trabajador.data.profile.local

import com.example.nexum_trabajador.data.local_data_source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/12/2026
 * @version 1.0
 */
@Singleton
class ProfileLocalDataSource @Inject constructor(
    private val dao: ProfileDao
) : LocalDataSource<ProfileEntity> {
    override suspend fun hasResource(): Boolean {
        return getAll().isNotEmpty();
    }

    override suspend fun getAll(): List<ProfileEntity> {
        return dao.getAll()
    }

    override fun observe(): Flow<List<ProfileEntity>> {
        return dao.observe()
    }

    override suspend fun replaceAll(resources: List<ProfileEntity>) {
        dao.replaceAll(resources)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun insertAll(profiles: List<ProfileEntity>) {
        dao.insertAll(profiles)
    }
}