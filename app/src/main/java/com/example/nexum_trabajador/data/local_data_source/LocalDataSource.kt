package com.example.nexum_trabajador.data.local_data_source

import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/9/2025
 * @version 1.0
 */
interface LocalDataSource<T> {
    suspend fun hasResource(): Boolean
    suspend fun getAll(): List<T>
    fun observe(): Flow<List<T>>
    suspend fun replaceAll(resources: List<T>)
    suspend fun clearAll()

}