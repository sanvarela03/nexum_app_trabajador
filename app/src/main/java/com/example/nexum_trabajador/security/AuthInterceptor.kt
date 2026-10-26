package com.example.nexum_trabajador.security

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. Inyectar metadatos e identidad del dispositivo en cada petición HTTP
        requestBuilder.header("X-Device-Id", tokenManager.getDeviceId())
        requestBuilder.header("X-Device-Model", tokenManager.getDeviceModel())
        requestBuilder.header("X-OS-Name", tokenManager.getOsName())
        requestBuilder.header("X-OS-Version", tokenManager.getOsVersion())
        requestBuilder.header("X-App-Version", tokenManager.getAppVersion())

        // 2. Inyectar Access Token vigente de forma síncrona si existe
        val accessToken = tokenManager.getAccessTokenSync()
        if (!accessToken.isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(requestBuilder.build())
    }
}
