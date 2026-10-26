package com.example.nexum_trabajador.security

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.util.UUID

class TokenManager(private val context: Context) {
    companion object {
        private const val TAG = "TokenManager"
        private const val PREFS_FILE = "secure_auth_prefs"
        private const val KEY_ACCESS_TOKEN = "jwt_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "auth-user"
        private const val KEY_DEVICE_ID = "device_id"
    }

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val prefs: SharedPreferences by lazy {
        try {
            createEncryptedPrefs()
        } catch (e: Exception) {
            Log.e(TAG, "Error inicializando EncryptedSharedPreferences, regenerando: ${e.message}")
            context.deleteSharedPreferences(PREFS_FILE)
            createEncryptedPrefs()
        }
    }

    private fun createEncryptedPrefs(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // StateFlows para mantener compatibilidad reactiva con ViewModels / Compose
    private val _accessTokenFlow by lazy { MutableStateFlow(getAccessTokenSync()) }
    private val _refreshTokenFlow by lazy { MutableStateFlow(getRefreshTokenSync()) }
    private val _userIdFlow by lazy { MutableStateFlow(getUserIdSync()) }

    // =========================================================================
    // ACCESO SÍNCRONO (Para OkHttp Interceptors / Authenticator sin runBlocking)
    // =========================================================================
    fun getAccessTokenSync(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshTokenSync(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)
    fun getUserIdSync(): Long? {
        if (prefs.contains(KEY_USER_ID)) {
            val id = prefs.getLong(KEY_USER_ID, -1L)
            if (id != -1L) return id
        }
        return getAccessTokenSync()?.let(::decodeUserIdFromJwt)
    }

    // =========================================================================
    // IDENTIDAD Y METADATOS DEL DISPOSITIVO
    // =========================================================================
    fun getOrCreateDeviceId(): String {
        var deviceId = prefs.getString(KEY_DEVICE_ID, null)
        if (deviceId.isNullOrBlank()) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
            Log.d(TAG, "Nuevo Device ID generado y persistido en Keystore: $deviceId")
        }
        return deviceId
    }

    fun getDeviceId(): String = getOrCreateDeviceId()

    fun getDeviceModel(): String = "${Build.MANUFACTURER} ${Build.MODEL}".trim()

    fun getOsName(): String = "Android"

    fun getOsVersion(): String = "${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"

    fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "1.0.0"
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener la versión de la aplicación: ${e.message}")
            "1.0.0"
        }
    }

    // =========================================================================
    // FLOWS REACTIVOS (Compatibilidad retroactiva con el código existente)
    // =========================================================================
    fun getAccessToken(): Flow<String?> = _accessTokenFlow.asStateFlow()
    fun getRefreshToken(): Flow<String?> = _refreshTokenFlow.asStateFlow()
    fun getUserId(): Flow<Long?> = _userIdFlow.asStateFlow()

    // =========================================================================
    // GESTIÓN DE TOKENS Y SESIÓN
    // =========================================================================
    suspend fun saveAccessToken(token: String?) {
        saveAccessTokenSync(token)
    }

    fun saveAccessTokenSync(token: String?) {
        if (token != null) {
            prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
            _accessTokenFlow.value = token
            decodeUserIdFromJwt(token)?.let { jwtId ->
                prefs.edit().putLong(KEY_USER_ID, jwtId).apply()
                _userIdFlow.value = jwtId
            }
            Log.d(TAG, "Access Token guardado en Keystore")
        } else {
            Log.e(TAG, "saveAccessToken llamado con token nulo!")
        }
    }

    suspend fun saveRefreshToken(token: String?) {
        saveRefreshTokenSync(token)
    }

    fun saveRefreshTokenSync(token: String?) {
        if (token != null) {
            prefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
            _refreshTokenFlow.value = token
            Log.d(TAG, "Refresh Token guardado en Keystore")
        } else {
            Log.e(TAG, "saveRefreshToken llamado con token nulo!")
        }
    }

    /**
     * Guarda ambos tokens atómicamente tras una rotación o login.
     */
    fun saveTokensSync(accessToken: String?, refreshToken: String?) {
        prefs.edit().apply {
            if (accessToken != null) putString(KEY_ACCESS_TOKEN, accessToken) else remove(KEY_ACCESS_TOKEN)
            if (refreshToken != null) putString(KEY_REFRESH_TOKEN, refreshToken) else remove(KEY_REFRESH_TOKEN)
            apply()
        }
        _accessTokenFlow.value = accessToken
        _refreshTokenFlow.value = refreshToken
        if (accessToken != null) {
            decodeUserIdFromJwt(accessToken)?.let { jwtId ->
                prefs.edit().putLong(KEY_USER_ID, jwtId).apply()
                _userIdFlow.value = jwtId
            }
        }
        Log.d(TAG, "Tokens guardados atómicamente en Keystore")
    }

    suspend fun saveTokens(accessToken: String?, refreshToken: String?) {
        saveTokensSync(accessToken, refreshToken)
    }

    suspend fun deleteAccessToken() {
        deleteAccessTokenSync()
    }

    fun deleteAccessTokenSync() {
        Log.d(TAG, "Eliminando Access Token de Keystore")
        prefs.edit().remove(KEY_ACCESS_TOKEN).apply()
        _accessTokenFlow.value = null
    }

    suspend fun deleteRefreshToken() {
        deleteRefreshTokenSync()
    }

    fun deleteRefreshTokenSync() {
        Log.d(TAG, "Eliminando Refresh Token de Keystore")
        prefs.edit().remove(KEY_REFRESH_TOKEN).apply()
        _refreshTokenFlow.value = null
    }

    suspend fun saveUserId(userId: Long?) {
        if (userId != null) {
            prefs.edit().putLong(KEY_USER_ID, userId).apply()
            _userIdFlow.value = userId
        } else {
            Log.e(TAG, "saveUserId llamado con null ID!")
        }
    }

    /**
     * Limpia la sesión completa del usuario (conservando el device_id de la instalación).
     */
    suspend fun clearAll() {
        clearAllSync()
    }

    fun clearAllSync() {
        Log.d(TAG, "Limpiando tokens y sesión en Keystore (conservando device_id)")
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_USER_ID)
            .apply()
        _accessTokenFlow.value = null
        _refreshTokenFlow.value = null
        _userIdFlow.value = null
    }

    private fun decodeUserIdFromJwt(token: String): Long? {
        return try {
            val payload = token.split(".").getOrNull(1) ?: return null
            val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            JSONObject(String(decoded, Charsets.UTF_8)).optLong("uid").takeIf { it != 0L }
        } catch (e: Exception) {
            Log.e(TAG, "No se pudo decodificar el uid del JWT: ${e.message}")
            null
        }
    }
}
