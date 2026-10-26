package com.example.nexum_trabajador.security

import android.util.Log
import com.example.nexum_trabajador.common.HOST_URL
import com.example.nexum_trabajador.data.auth.remote.AuthApi
import com.example.nexum_trabajador.data.auth.remote.payload.req.RefreshReq
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
) : Authenticator {

    companion object {
        private const val TAG = "AuthAuthenticator"
        private const val MAX_REFRESH_ATTEMPTS = 2
        private val refreshMutex = Mutex()
    }

    private val refreshService: AuthApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val req = chain.request().newBuilder()
                    .header("X-Device-Id", tokenManager.getDeviceId())
                    .header("X-Device-Model", tokenManager.getDeviceModel())
                    .header("X-OS-Name", tokenManager.getOsName())
                    .header("X-OS-Version", tokenManager.getOsVersion())
                    .header("X-App-Version", tokenManager.getAppVersion())
                    .build()
                chain.proceed(req)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(HOST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. Evitar bucles infinitos si la petición ya fue reintentada más del límite
        if (refreshAttempts(response) >= MAX_REFRESH_ATTEMPTS) {
            Log.w(TAG, "Se alcanzó el máximo de intentos de refresh ($MAX_REFRESH_ATTEMPTS). Abortando.")
            return null
        }

        val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")?.trim()

        // 2. Fast-path: Si otra corrutina/hilo ya renovó el token mientras esta petición fallaba
        val currentAccessToken = tokenManager.getAccessTokenSync()
        if (!currentAccessToken.isNullOrBlank() && currentAccessToken != requestToken) {
            Log.d(TAG, "Token ya fue renovado por otra petición concurrente. Reintentando...")
            return response.request.newBuilder()
                .header("Authorization", "Bearer $currentAccessToken")
                .build()
        }

        // 3. Sincronización estricta con Mutex para evitar colisiones RTR concurrentes
        return runBlocking {
            refreshMutex.withLock {
                // Doble comprobación dentro del lock por si otro hilo acaba de renovar
                val freshAccessToken = tokenManager.getAccessTokenSync()
                if (!freshAccessToken.isNullOrBlank() && freshAccessToken != requestToken) {
                    Log.d(TAG, "Token renovado mientras se esperaba el lock. Reintentando...")
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $freshAccessToken")
                        .build()
                }

                val storedRefreshToken = tokenManager.getRefreshTokenSync()
                if (storedRefreshToken.isNullOrBlank()) {
                    Log.w(TAG, "No hay refresh token almacenado. Limpiando sesión.")
                    tokenManager.clearAllSync()
                    return@runBlocking null
                }

                Log.d(TAG, "Ejecutando rotación de token (RTR) con backend...")
                val refreshResult = try {
                    refreshService.refreshToken(RefreshReq(storedRefreshToken))
                } catch (e: Exception) {
                    Log.e(TAG, "Excepción de red al refrescar token: ${e.message}")
                    return@runBlocking null
                }

                if (refreshResult.isSuccessful && refreshResult.body() != null) {
                    val body = refreshResult.body()!!
                    Log.d(TAG, "Tokens rotados exitosamente. Guardando atómicamente nuevo access y refresh token.")
                    // CRUCIAL PARA RTR: Guardar AMBOS tokens (el nuevo Access Token Y el nuevo Refresh Token rotado)
                    tokenManager.saveTokensSync(body.accessToken, body.refreshToken)

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${body.accessToken}")
                        .build()
                } else {
                    val code = refreshResult.code()
                    Log.e(TAG, "Error en refresh token: HTTP $code. Limpiando sesión completa.")
                    if (code in 400..403) {
                        tokenManager.clearAllSync()
                    }
                    null
                }
            }
        }
    }

    private fun refreshAttempts(response: Response): Int {
        var count = 0
        var prior: Response? = response
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
