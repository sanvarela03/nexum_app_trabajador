package com.example.nexum_trabajador.data.job_application.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JobApplicationDraftDao {
    @Query("SELECT * FROM JobApplicationDraftEntity WHERE jobOfferId = :jobOfferId")
    suspend fun getDraft(jobOfferId: Long): JobApplicationDraftEntity?

    @Query("SELECT * FROM JobApplicationDraftEntity WHERE jobOfferId != :currentJobOfferId ORDER BY updatedAt DESC")
    suspend fun getAllOtherDrafts(currentJobOfferId: Long): List<JobApplicationDraftEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDraft(draft: JobApplicationDraftEntity)

    @Query("DELETE FROM JobApplicationDraftEntity WHERE jobOfferId = :jobOfferId")
    suspend fun deleteDraft(jobOfferId: Long)

    @Query("DELETE FROM JobApplicationDraftEntity WHERE jobOfferUuid = :jobOfferUuid")
    suspend fun deleteDraftByUuid(jobOfferUuid: String)
}
