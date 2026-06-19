package com.example.nexum_trabajador.common


import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Encapsula la lógica genérica para actualizar un recurso desde una fuente remota y guardarlo en una caché local.
 *
 * @param RemoteType El tipo de dato que se recibe de la API (DTO).
 * @param fetchFromRemote Booleano para forzar la actualización desde la red.
 * @param checkCache Suspend function para verificar si la caché ya tiene datos válidos.
 * @param remoteCall Suspend function que realiza la llamada a la API y devuelve un [ApiResponse].
 * @param saveToCache Suspend function para guardar los datos remotos en la caché local.
 * @param clearCache Suspend function opcional para limpiar la caché si la respuesta remota está vacía.
 * @param dispatcher El CoroutineDispatcher en el que se ejecutará el flow.
 */
inline fun <RemoteType> updateResourceFlow(
    fetchFromRemote: Boolean,
    crossinline checkCache: suspend () -> Boolean,
    crossinline remoteCall: suspend () -> Flow<ApiResponse<RemoteType>>,
    crossinline saveToCache: suspend (RemoteType) -> Unit,
    crossinline clearCache: suspend () -> Unit = {},
    dispatcher: CoroutineDispatcher
): Flow<ApiResponse<Unit>> = flow {
    val cacheExists = checkCache()
    if (cacheExists && !fetchFromRemote) {
        emit(ApiResponse.Success(Unit))
        return@flow
    }

    // Cargar desde remoto
    emit(ApiResponse.Loading)
    remoteCall().collect { apiRes ->
        when (apiRes) {
            is ApiResponse.Success -> {
                val data = apiRes.data
                // Asumimos que si los datos son una colección, podemos verificar si está vacía.
                if (data is Collection<*> && data.isEmpty()) {
                    clearCache()
                } else {
                    saveToCache(data)
                }
                emit(ApiResponse.Success(Unit))
            }
            is ApiResponse.Error -> emit(apiRes)
            is ApiResponse.Failure -> emit(apiRes)
            ApiResponse.Loading -> { /* No emitir loading de nuevo si ya estamos en ello */ }
        }
    }
}.flowOn(dispatcher)
