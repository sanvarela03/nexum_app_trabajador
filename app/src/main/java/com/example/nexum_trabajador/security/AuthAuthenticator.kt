package com.example.nexum_trabajador.security

import android.util.Log
import com.example.nexum_trabajador.common.HOST_URL
import com.example.nexum_trabajador.data.auth.remote.AuthApi
import com.example.nexum_trabajador.data.auth.remote.payload.req.RefreshReq
import com.example.nexum_trabajador.data.auth.remote.payload.res.RefreshRes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthenticator
@Inject
constructor(
    private val tokenManager: TokenManager
) : Authenticator {

    companion object {
        private const val MAX_REFRESH_ATTEMPTS = 3
    }

    override fun authenticate(route: Route?, response: Response): Request? {

        if (refreshAttempts(response) >= MAX_REFRESH_ATTEMPTS) {
            Log.w("AuthAuthenticator", "Se alcanzó el máximo de reintentos de refresh (${MAX_REFRESH_ATTEMPTS}).")
            return null
        }

        val refreshToken = runBlocking {
            tokenManager.getRefreshToken().first()
        } ?: return null

        return runBlocking {
            val newAccessToken = getNewAccessToken(refreshToken)

            if (!newAccessToken.isSuccessful || newAccessToken.body() == null) {
                tokenManager.deleteAccessToken()
            }

            newAccessToken.body()?.let {
                tokenManager.saveAccessToken(it.accessToken)
                response.request
                    .newBuilder()
                    .header("Authorization", "Bearer ${it.accessToken}")
                    .build()
            }
        }
    }

    private suspend fun getNewAccessToken(refreshToken: String?): retrofit2.Response<RefreshRes> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl(HOST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(AuthApi::class.java)

        Log.d("STORED REFRESH TOKEN: ", "$refreshToken")
        val refreshTokenRequest = RefreshReq(refreshToken!!)
        return service.refreshToken(refreshTokenRequest)
    }

    /**
     * Cuenta cuántas veces se ha llamado a authenticate para esta misma petición,
     * recorriendo priorResponse.
     */
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
