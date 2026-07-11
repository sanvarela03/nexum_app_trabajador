package com.example.nexum_trabajador.security

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.nexum_trabajador.common.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject

class TokenManager(private val context: Context) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_KEY = longPreferencesKey("auth-user")
        private val USER_ROLES = stringSetPreferencesKey("auth-user-roles")
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }
    }

    suspend fun saveRefreshToken(token: String?) {
        if (token != null) {
            Log.d("TokenManager", "Saving Refresh Token")
            context.dataStore.edit { preferences ->
                preferences[REFRESH_TOKEN_KEY] = token
            }
        } else {
            Log.e("TokenManager", "saveRefreshToken was called with a null token!")
        }
    }

    suspend fun deleteRefreshToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    }

    suspend fun saveAccessToken(token: String?) {
        if (token != null) {
            Log.d("TokenManager", "Saving Access Token: $token")
            context.dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = token
            }
        } else {
            Log.e("TokenManager", "saveAccessToken was called with a null token!")
        }
    }

    suspend fun deleteAccessToken() {
        Log.d("TokenManager ->", "deleteAccessToken")
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }

    suspend fun saveUserId(userId: Long?) {
        if (userId != null) {
            context.dataStore.edit { preferences ->
                preferences[USER_KEY] = userId
            }
        } else {
            Log.e("TokenManager", "saveUserId was called with a null ID!")
        }
    }

    /**
     * Deriva el id del usuario a partir del claim `uid` del access token vigente,
     * en lugar de un valor persistido aparte (USER_KEY), que puede quedar
     * desincronizado entre sesiones/cuentas (p. ej. tras un refresh de token
     * o un cambio de cuenta que no reescribe ese campo).
     */
    fun getUserId(): Flow<Long?> {
        return getAccessToken().map { token -> token?.let(::decodeUserIdFromJwt) }
    }

    private fun decodeUserIdFromJwt(token: String): Long? {
        return try {
            val payload = token.split(".").getOrNull(1) ?: return null
            val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            JSONObject(String(decoded, Charsets.UTF_8)).optLong("uid").takeIf { it != 0L }
        } catch (e: Exception) {
            Log.e("TokenManager", "No se pudo decodificar el uid del JWT: ${e.message}")
            null
        }
    }
}
