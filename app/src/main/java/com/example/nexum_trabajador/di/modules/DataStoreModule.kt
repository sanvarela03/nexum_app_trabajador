package com.example.nexum_trabajador.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.nexum_trabajador.common.dataStore
import com.example.nexum_trabajador.data.local_storage.AsyncStorage
import com.example.nexum_trabajador.security.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Singleton
    @Provides
    fun provideAsyncStorage(preferences: DataStore<Preferences>): AsyncStorage =
        AsyncStorage(preferences)

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)
}
