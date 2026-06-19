package com.example.nexum_trabajador.data.local_storage

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz genérica para operaciones básicas de base de datos.
 * @param T La clase de la entidad (Entity).
 */
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)
    @Update
    suspend fun update(entity: T)
    @Delete
    suspend fun delete(entity: T)
    suspend fun clearAll()
    fun observe(): Flow<List<T>>
}
