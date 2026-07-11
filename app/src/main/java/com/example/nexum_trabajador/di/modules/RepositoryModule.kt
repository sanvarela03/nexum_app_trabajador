package com.example.nexum_trabajador.di.modules

import com.example.nexum_trabajador.data.auth.AuthRepositoryImpl
import com.example.nexum_trabajador.data.auth.remote.AuthApi
import com.example.nexum_trabajador.data.country.CountryRepositoryImpl
import com.example.nexum_trabajador.data.country.local.CountryLocalDataSource
import com.example.nexum_trabajador.data.country.remote.CountryRemoteDataSource
import com.example.nexum_trabajador.data.market_location.MarketLocationRepositoryImpl
import com.example.nexum_trabajador.data.market_location.local.MarketLocationLocalDataSource
import com.example.nexum_trabajador.data.market_location.remote.MarketLocationRemoteDataSource
import com.example.nexum_trabajador.data.message.MessagingRepositoryImpl
import com.example.nexum_trabajador.data.notification.NotificationRepositoryImpl
import com.example.nexum_trabajador.data.notification.local.NotificationLocalDataSource
import com.example.nexum_trabajador.data.profile.ProfileRepositoryImpl
import com.example.nexum_trabajador.data.profile.local.ProfileLocalDataSource
import com.example.nexum_trabajador.data.profile.remote.ProfileRemoteDataSource
import com.example.nexum_trabajador.data.worker.WorkerRepositoryImpl
import com.example.nexum_trabajador.data.worker.local.WorkerLocalDataSource
import com.example.nexum_trabajador.data.worker.remote.WorkerRemoteDataSource
import com.example.nexum_trabajador.domain.repository.AuthRepository
import com.example.nexum_trabajador.domain.repository.CountryRepository
import com.example.nexum_trabajador.domain.repository.MarketLocationRepository
import com.example.nexum_trabajador.domain.repository.MessagingRepository
import com.example.nexum_trabajador.domain.repository.NotificationRepository
import com.example.nexum_trabajador.domain.repository.JobApplicationRepository
import com.example.nexum_trabajador.domain.repository.ProfileRepository
import com.example.nexum_trabajador.domain.repository.WorkerRepository
import com.example.nexum_trabajador.security.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        tokenManager: TokenManager,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): AuthRepository = AuthRepositoryImpl(
        authApi = api,
        tokenManager = tokenManager,
        dispatcher = dispatcher
    )

    @Provides
    @Singleton
    fun provideCountryRepository(
        localDataSource: CountryLocalDataSource,
        remoteDataSource: CountryRemoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CountryRepository = CountryRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        dispatcher = dispatcher
    )

    @Provides
    @Singleton
    fun provideMarketLocationRepository(
        localDataSource: MarketLocationLocalDataSource,
        remoteDataSource: MarketLocationRemoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): MarketLocationRepository =
        MarketLocationRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            dispatcher = dispatcher
        )

    @Provides
    @Singleton
    fun provideWorkerRepository(
        localDataSource: WorkerLocalDataSource,
        remoteDataSource: WorkerRemoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): WorkerRepository = WorkerRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        dispatcher = dispatcher
    )

    @Provides
    @Singleton
    fun provideNotificationRepository(
        localDataSource: NotificationLocalDataSource
    ): NotificationRepository = NotificationRepositoryImpl(
        localDataSource = localDataSource
    )

    @Provides
    @Singleton
    fun provideMessagingRepository(
        impl: MessagingRepositoryImpl
    ): MessagingRepository = impl

    @Provides
    @Singleton
    fun provideProfileRepository(
        remote: ProfileRemoteDataSource,
        local: ProfileLocalDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ProfileRepository = ProfileRepositoryImpl(
        remote = remote,
        local = local,
        dispatcher = dispatcher
    )

    @Provides
    @Singleton
    fun provideJobOfferRepository(
        api: com.example.nexum_trabajador.data.job_offer.remote.JobOfferApi
    ): com.example.nexum_trabajador.domain.repository.JobOfferRepository = 
        com.example.nexum_trabajador.data.job_offer.repository.JobOfferRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideJobApplicationRepository(
        api: com.example.nexum_trabajador.data.job_application.remote.JobApplicationApi,
        draftDao: com.example.nexum_trabajador.data.job_application.local.JobApplicationDraftDao
    ): JobApplicationRepository = 
        com.example.nexum_trabajador.data.job_application.repository.JobApplicationRepositoryImpl(api, draftDao)
}
