package com.example.nexum_trabajador.di.modules

import com.example.nexum_trabajador.data.country.local.CountryDao
import com.example.nexum_trabajador.data.local.db.WorkerDb
import com.example.nexum_trabajador.data.market_location.local.MarketLocationDao
import com.example.nexum_trabajador.data.notification.local.NotificationDao
import com.example.nexum_trabajador.data.worker.local.WorkerDao
import com.example.nexum_trabajador.data.job_application.local.JobApplicationDraftDao
import com.example.nexum_trabajador.data.profile.local.ProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideCountryDao(workerDb: WorkerDb): CountryDao = workerDb.countryDao

    @Provides
    @Singleton
    fun provideMarketLocationDao(workerDb: WorkerDb): MarketLocationDao = workerDb.marketLocationDao

    @Provides
    @Singleton
    fun provideWorkerDao(workerDb: WorkerDb): WorkerDao = workerDb.workerDao

    @Provides
    @Singleton
    fun provideNotificationDao(workerDb: WorkerDb): NotificationDao = workerDb.notificationDao

    @Provides
    @Singleton
    fun provideJobApplicationDraftDao(workerDb: WorkerDb): JobApplicationDraftDao = workerDb.jobApplicationDraftDao

    @Provides
    @Singleton
    fun provideProfileDao(workerDb: WorkerDb): ProfileDao = workerDb.profileDao
}