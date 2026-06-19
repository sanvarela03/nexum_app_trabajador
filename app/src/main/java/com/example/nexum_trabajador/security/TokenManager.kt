package com.example.nexum_trabajador.security

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.nexum_trabajador.common.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    fun getUserId(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_KEY]
        }
    }
}
