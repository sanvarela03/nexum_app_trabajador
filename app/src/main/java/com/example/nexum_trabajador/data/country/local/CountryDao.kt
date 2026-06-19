package com.example.nexum_trabajador.data.country.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(country: CountryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(countries: List<CountryEntity>)

    //para limpiar la tabla
    @Query("DELETE FROM countries")
    suspend fun clearAll()

    //para observar el recurso
    @Query("SELECT * FROM countries")
    fun observe(): Flow<List<CountryEntity>>

    //para obtener el recurso
    @Query("SELECT * FROM countries")
    suspend fun getAll(): List<CountryEntity>

    //para remplazar
    @Transaction
    suspend fun replaceAll(countries: List<CountryEntity>) {
        clearAll()
        saveAll(countries)
    }
}