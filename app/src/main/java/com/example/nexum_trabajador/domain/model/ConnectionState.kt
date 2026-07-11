package com.example.nexum_trabajador.domain.model

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.1
 */
enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    RECONNECTING,
    CONNECTED,
    ERROR
}