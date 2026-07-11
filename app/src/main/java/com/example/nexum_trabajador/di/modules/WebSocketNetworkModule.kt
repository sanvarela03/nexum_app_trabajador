package com.example.nexum_trabajador.di.modules

import com.example.nexum_trabajador.common.BASE_URL
import com.example.nexum_trabajador.common.WS_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebSocketUrl

@Module
@InstallIn(SingletonComponent::class)
object WebSocketNetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(json: Json, okHttpClient: OkHttpClient): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                preconfigured = okHttpClient
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(WebSockets)
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
            encodeDefaults = true
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    @WebSocketUrl
    fun provideWebSocketUrl(): String = WS_URL

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = BASE_URL
}
