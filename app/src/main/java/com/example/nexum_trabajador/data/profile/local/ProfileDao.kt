package com.example.nexum_trabajador.data.profile.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.nexum_trabajador.data.local_storage.BaseDao
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/12/2026
 * @version 1.0
 */
@Dao
interface ProfileDao : BaseDao<ProfileEntity> {

    @Query("SELECT * FROM ProfileEntity")
    suspend fun getAll(): List<ProfileEntity>

    @Query("SELECT * FROM ProfileEntity WHERE id = :id")
    suspend fun getById(id: String): ProfileEntity?

    @Query("SELECT * FROM ProfileEntity")
    override fun observe(): Flow<List<ProfileEntity>>

    @Query("DELETE FROM ProfileEntity")
    override suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(entities: List<ProfileEntity>) {
        clearAll()
        insertAll(entities)
    }
}