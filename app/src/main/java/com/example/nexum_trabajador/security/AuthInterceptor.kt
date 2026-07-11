package com.example.nexum_trabajador.security

import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor
@Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val accessToken = runBlocking {
            tokenManager.getAccessToken().first()
        }

        Log.d("INTERCEP THOR !!!", "$accessToken")

        val request = chain.request().newBuilder()
        request.header("Authorization", "Bearer $accessToken")
        return chain.proceed(request.build())
    }
}
