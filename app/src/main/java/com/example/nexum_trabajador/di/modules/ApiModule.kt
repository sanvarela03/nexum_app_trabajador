package com.example.nexum_trabajador.di.modules

import com.example.nexum_trabajador.common.HOST_URL
import com.example.nexum_trabajador.data.auth.remote.AuthApi
import com.example.nexum_trabajador.data.country.remote.CountryApi
import com.example.nexum_trabajador.data.market_location.remote.MarketLocationApi
import com.example.nexum_trabajador.data.profile.remote.ProfileApi
import com.example.nexum_trabajador.data.worker.remote.WorkerApi
import com.example.nexum_trabajador.security.AuthAuthenticator
import com.example.nexum_trabajador.security.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(HOST_URL)
            .addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideAuthApi(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): AuthApi =
        retrofit
            .client(okHttpClient) // para adjuntar el token en el token en el sign out
            .build()
            .create(AuthApi::class.java)


    @Provides
    @Singleton
    fun provideCountryApi(
        retrofit: Retrofit.Builder,
    ): CountryApi = retrofit
        .build()
        .create(CountryApi::class.java)

    @Provides
    @Singleton
    fun provideMarketLocationApi(
        retrofit: Retrofit.Builder,
    ): MarketLocationApi = retrofit
        .build()
        .create(MarketLocationApi::class.java)


    @Provides
    @Singleton
    fun provideWorkerApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): WorkerApi = retrofit
        .client(okHttpClient)
        .build()
        .create(WorkerApi::class.java)

    @Provides
    @Singleton
    fun provideJobOfferApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): com.example.nexum_trabajador.data.job_offer.remote.JobOfferApi = retrofit
        .client(okHttpClient)
        .build()
        .create(com.example.nexum_trabajador.data.job_offer.remote.JobOfferApi::class.java)

    @Provides
    @Singleton
    fun provideJobApplicationApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): com.example.nexum_trabajador.data.job_application.remote.JobApplicationApi = retrofit
        .client(okHttpClient)
        .build()
        .create(com.example.nexum_trabajador.data.job_application.remote.JobApplicationApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): ProfileApi = retrofit
        .client(okHttpClient)
        .build()
        .create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideContractApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): com.example.nexum_trabajador.data.contract.remote.ContractApi = retrofit
        .client(okHttpClient)
        .build()
        .create(com.example.nexum_trabajador.data.contract.remote.ContractApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): com.example.nexum_trabajador.data.user.remote.UserApi = retrofit
        .client(okHttpClient)
        .build()
        .create(com.example.nexum_trabajador.data.user.remote.UserApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(
        retrofit: Retrofit.Builder,
    ): com.example.nexum_trabajador.data.category.remote.CategoryApi = retrofit
        .build()
        .create(com.example.nexum_trabajador.data.category.remote.CategoryApi::class.java)

    @Provides
    @Singleton
    fun provideJobOccupationApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): com.example.nexum_trabajador.data.job_occupation.remote.JobOccupationApi = retrofit
        .client(okHttpClient)
        .build()
        .create(com.example.nexum_trabajador.data.job_occupation.remote.JobOccupationApi::class.java)

    @Provides
    @Singleton
    fun provideInsuranceApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): com.example.nexum_trabajador.data.insurance.remote.InsuranceApi = retrofit
        .client(okHttpClient)
        .build()
        .create(com.example.nexum_trabajador.data.insurance.remote.InsuranceApi::class.java)

    @Provides
    @Singleton
    fun provideCertificationApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): com.example.nexum_trabajador.data.certification.remote.CertificationApi = retrofit
        .client(okHttpClient)
        .build()
        .create(com.example.nexum_trabajador.data.certification.remote.CertificationApi::class.java)

    @Provides
    @Singleton
    fun provideSkillSetApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): com.example.nexum_trabajador.data.skill_set.remote.SkillSetApi = retrofit
        .client(okHttpClient)
        .build()
        .create(com.example.nexum_trabajador.data.skill_set.remote.SkillSetApi::class.java)
}
