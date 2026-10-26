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

    @Provides
    @Singleton
    fun provideContractRepository(
        remoteDataSource: com.example.nexum_trabajador.data.contract.remote.ContractRemoteDataSource
    ): com.example.nexum_trabajador.domain.repository.ContractRepository =
        com.example.nexum_trabajador.data.contract.repository.ContractRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideUserRepository(
        userApi: com.example.nexum_trabajador.data.user.remote.UserApi
    ): com.example.nexum_trabajador.domain.repository.UserRepository =
        com.example.nexum_trabajador.data.user.UserRepositoryImpl(userApi)

    @Provides
    @Singleton
    fun provideCategoryRepository(
        remoteDataSource: com.example.nexum_trabajador.data.category.remote.CategoryRemoteDataSource
    ): com.example.nexum_trabajador.domain.repository.CategoryRepository =
        com.example.nexum_trabajador.data.category.CategoryRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideJobOccupationRepository(
        remoteDataSource: com.example.nexum_trabajador.data.job_occupation.remote.JobOccupationRemoteDataSource
    ): com.example.nexum_trabajador.domain.repository.JobOccupationRepository =
        com.example.nexum_trabajador.data.job_occupation.repository.JobOccupationRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideInsuranceRepository(
        remoteDataSource: com.example.nexum_trabajador.data.insurance.remote.InsuranceRemoteDataSource
    ): com.example.nexum_trabajador.domain.repository.InsuranceRepository =
        com.example.nexum_trabajador.data.insurance.repository.InsuranceRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideCertificationRepository(
        remoteDataSource: com.example.nexum_trabajador.data.certification.remote.CertificationRemoteDataSource
    ): com.example.nexum_trabajador.domain.repository.CertificationRepository =
        com.example.nexum_trabajador.data.certification.repository.CertificationRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideSkillSetRepository(
        remoteDataSource: com.example.nexum_trabajador.data.skill_set.remote.SkillSetRemoteDataSource
    ): com.example.nexum_trabajador.domain.repository.SkillSetRepository =
        com.example.nexum_trabajador.data.skill_set.repository.SkillSetRepositoryImpl(remoteDataSource)
}
