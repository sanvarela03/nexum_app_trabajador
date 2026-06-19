package com.example.nexum_trabajador.data.worker

import com.example.nexum_trabajador.common.updateResourceFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.global_payload.res.MessageRes
import com.example.nexum_trabajador.data.worker.local.WorkerLocalDataSource
import com.example.nexum_trabajador.data.worker.mapper.WorkerMapper
import com.example.nexum_trabajador.data.worker.remote.WorkerRemoteDataSource
import com.example.nexum_trabajador.di.modules.IoDispatcher
import com.example.nexum_trabajador.domain.model.Worker
import com.example.nexum_trabajador.domain.repository.WorkerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
class WorkerRepositoryImpl @Inject constructor(
    private val localDataSource: WorkerLocalDataSource,
    private val remoteDataSource: WorkerRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : WorkerRepository {
    override fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> {
        return updateResourceFlow(
            fetchFromRemote = fetchFromRemote,
            checkCache = { localDataSource.hasResource() },
            remoteCall = { remoteDataSource.getWorkers() },
            saveToCache = { localDataSource.replaceAll(listOf(WorkerMapper.toEntity(it))) },
            clearCache = { localDataSource.clearAll() },
            dispatcher = dispatcher
        )
    }

    override fun observe(): Flow<Worker?> {
        return localDataSource
            .observe()
            .map { WorkerMapper.toDomain(it).firstOrNull() }
    }

    override fun updateWorkerDetails(worker: Worker): Flow<ApiResponse<MessageRes>> {
        return remoteDataSource.updateWorkerDetails(WorkerMapper.toUpdateReq(worker))
    }

}
