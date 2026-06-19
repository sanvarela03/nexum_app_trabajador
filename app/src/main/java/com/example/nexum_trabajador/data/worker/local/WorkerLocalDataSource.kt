package com.example.nexum_trabajador.data.worker.local

import com.example.nexum_trabajador.data.local_data_source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/18/2025
 * @version 1.0
 */
@Singleton
class WorkerLocalDataSource @Inject constructor(
    private val workerDao: WorkerDao
) : LocalDataSource<WorkerEntity> {
    override suspend fun hasResource(): Boolean {
        return getAll().isNotEmpty()
    }

    override suspend fun getAll(): List<WorkerEntity> {
        return workerDao.getAll()
    }

    override fun observe(): Flow<List<WorkerEntity>> {
        return workerDao.observe()
    }

    override suspend fun replaceAll(resources: List<WorkerEntity>) {
        workerDao.replaceAll(resources)
    }

    override suspend fun clearAll() {
        workerDao.clearAll()
    }
}