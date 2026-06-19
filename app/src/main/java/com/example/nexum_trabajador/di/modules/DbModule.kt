package com.example.nexum_trabajador.di.modules

import android.app.Application
import androidx.room.Room
import com.example.nexum_trabajador.data.local.db.WorkerDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideWorkerDb(app: Application): WorkerDb {
        return Room.databaseBuilder(
            app,
            WorkerDb::class.java,
            "worker_db"
        ).fallbackToDestructiveMigration().build()
    }
}