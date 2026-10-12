package com.example.nexum_trabajador.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nexum_trabajador.data.country.local.CountryDao
import com.example.nexum_trabajador.data.country.local.CountryEntity
import com.example.nexum_trabajador.data.market_location.local.MarketLocationDao
import com.example.nexum_trabajador.data.market_location.local.MarketLocationEntity
import com.example.nexum_trabajador.data.notification.local.NotificationDao
import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import com.example.nexum_trabajador.data.worker.local.WorkerDao
import com.example.nexum_trabajador.data.worker.local.WorkerEntity
import com.example.nexum_trabajador.data.job_application.local.JobApplicationDraftEntity
import com.example.nexum_trabajador.data.job_application.local.JobApplicationDraftDao
import com.example.nexum_trabajador.data.profile.local.ProfileDao
import com.example.nexum_trabajador.data.profile.local.ProfileEntity

@Database(
    entities = [
        CountryEntity::class,
        MarketLocationEntity::class,
        WorkerEntity::class,
        NotificationEntity::class,
        JobApplicationDraftEntity::class,
        ProfileEntity::class
    ],
    version = 7
)
abstract class WorkerDb : RoomDatabase() {
    abstract val countryDao: CountryDao
    abstract val marketLocationDao: MarketLocationDao
    abstract val workerDao: WorkerDao
    abstract val notificationDao: NotificationDao
    abstract val jobApplicationDraftDao: JobApplicationDraftDao
    abstract val profileDao: ProfileDao
}
