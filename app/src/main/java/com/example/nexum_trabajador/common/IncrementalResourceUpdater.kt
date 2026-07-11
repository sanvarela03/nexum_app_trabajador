package com.example.nexum_trabajador.common

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Encapsula la lógica para actualizar recursos de forma incremental sin borrar la caché existente.
 */
inline fun <RemoteType> updateIncrementalResourceFlow(
    fetchFromRemote: Boolean,
    crossinline remoteCall: suspend () -> Flow<ApiResponse<RemoteType>>,
    crossinline saveToCache: suspend (RemoteType) -> Unit,
    dispatcher: CoroutineDispatcher
): Flow<ApiResponse<Unit>> = flow {
    if (!fetchFromRemote) {
        emit(ApiResponse.Success(Unit))
        return@flow
    }

    emit(ApiResponse.Loading)
    remoteCall().collect { apiRes ->
        when (apiRes) {
            is ApiResponse.Success -> {
                saveToCache(apiRes.data)
                emit(ApiResponse.Success(Unit))
            }
            is ApiResponse.Error -> emit(apiRes)
            is ApiResponse.Failure -> emit(apiRes)
            ApiResponse.Loading -> {}
        }
    }
}.flowOn(dispatcher)
