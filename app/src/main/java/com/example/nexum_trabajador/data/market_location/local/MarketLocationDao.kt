package com.example.nexum_trabajador.data.market_location.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 04/09/2025
 * @version 1.0
 */
@Dao
interface MarketLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(location: MarketLocationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(locations: List<MarketLocationEntity>)

    @Query("SELECT * FROM MarketLocationEntity")
    fun observe(): Flow<List<MarketLocationEntity>>

    @Query("SELECT * FROM MarketLocationEntity")
    suspend fun getAll(): List<MarketLocationEntity>

    @Transaction
    @Query("DELETE FROM MarketLocationEntity")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(locations: List<MarketLocationEntity>) {
        clearAll()
        saveAll(locations)
    }

}