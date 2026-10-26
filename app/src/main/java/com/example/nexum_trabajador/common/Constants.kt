package com.example.nexum_trabajador.common

/**
 * Constantes globales de la aplicación
 */
// Local IP address for the Android Emulator to connect to the host machine's localhost
private val IP = "10.0.2.2"
private val PORT = "8080"
private val MSG_PORT = "8081"

private val LOCAL_URL = "http://$IP:$PORT"
private val LOCAL_MSG_URL = "http://$IP:$MSG_PORT"
private val LOCAL_WS_URL = "ws://$IP:$MSG_PORT"

private val REMOTE_URL = "https://nexum-be.onrender.com"
private val REMOTE_MSG_URL = "https://nexum-msg-be.onrender.com"
private val REMOTE_WS_URL = "wss://nexum-msg-be.onrender.com"

// Cambiar a `true` para apuntar al backend local (host machine),
// o `false` para apuntar al backend remoto en Render.
val useLocal = false

val HOST_URL = if (useLocal) LOCAL_URL else REMOTE_URL
val MSG_URL = if (useLocal) LOCAL_MSG_URL else REMOTE_MSG_URL
val BASE_URL = MSG_URL
val WS_URL = if (useLocal) LOCAL_WS_URL else REMOTE_WS_URL

const val APP_KEY = "f8e7d6c5-4b3a-2d1c-0b9a-8f7e6d5c4b3a"

