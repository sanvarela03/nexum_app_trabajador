package com.example.nexum_trabajador.data.notification.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notificationEntity: NotificationEntity)

    @Query("SELECT * FROM NotificationEntity WHERE id = :id")
    @Transaction
    fun get(id: Int): Flow<NotificationEntity>

    @Query("SELECT * FROM NotificationEntity")
    @Transaction
    fun getAll(): Flow<List<NotificationEntity>>

    @Delete
    suspend fun delete(notificationEntity: NotificationEntity)

    @Transaction
    @Query("DELETE FROM NotificationEntity WHERE id = :notificationId")
    suspend fun deleteById(notificationId : Int)

    @Query("SELECT * FROM NotificationEntity WHERE id = :id")
    suspend fun getOne(id: Int): NotificationEntity?
}