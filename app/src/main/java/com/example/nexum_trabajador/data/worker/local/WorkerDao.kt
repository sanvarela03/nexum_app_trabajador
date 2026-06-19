package com.example.nexum_trabajador.data.worker.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.nexum_trabajador.data.local_storage.BaseDao
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/18/2025
 * @version 1.0
 */
@Dao
interface WorkerDao : BaseDao<WorkerEntity> {
    @Query("DELETE FROM workers")
    override suspend fun clearAll()
    @Query("SELECT * FROM workers")
    override fun observe(): Flow<List<WorkerEntity>>
    @Query("SELECT * FROM workers")
    suspend fun getAll(): List<WorkerEntity>
    @Transaction
    suspend fun replaceAll(workers: List<WorkerEntity>) {
        clearAll()
        insertAll(workers)
    }
}
