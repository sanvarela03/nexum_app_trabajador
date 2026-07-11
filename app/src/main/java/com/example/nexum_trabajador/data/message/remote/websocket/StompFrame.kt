package com.example.nexum_trabajador.data.message.remote.websocket

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.1
 */
data class StompFrame(
    val command: String,
    val headers: Map<String, String> = emptyMap(),
    val body: String = ""
) {
    fun toRawString(): String = buildString {
        append(command.trim()).append('\n')
        headers.forEach { (key, value) ->
            append(key.trim()).append(':').append(value.trim()).append('\n')
        }
        append('\n')
        append(body)
        append('\u0000') // NULL terminator
    }

    companion object {
        fun fromRawString(raw: String): StompFrame? {
            if (raw.isBlank() || raw == "\n" || raw == "\r\n") return null

            val lines = raw.split("\n")
            val command = lines.firstOrNull()?.trim() ?: return null
            if (command.isEmpty()) return null

            val headers = mutableMapOf<String, String>()
            var bodyStartIndex = 1

            for (i in 1 until lines.size) {
                val line = lines[i].trim()
                if (line.isEmpty()) {
                    bodyStartIndex = i + 1
                    break
                }
                val parts = lines[i].split(":", limit = 2)
                if (parts.size == 2) {
                    headers[parts[0].trim()] = parts[1].trim()
                }
            }

            val body = if (bodyStartIndex < lines.size) {
                lines.subList(bodyStartIndex, lines.size)
                    .joinToString("\n")
                    .replace("\u0000", "")
                    .trim()
            } else ""

            return StompFrame(command, headers, body)
        }
    }
}
