package com.example.nexum_trabajador.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime {
    return when {
        this.contains("T") -> {
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            LocalDateTime.parse(this, pattern)
        }
        this.length == 10 -> { // yyyy-MM-dd
             val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
             LocalDate.parse(this, pattern).atStartOfDay()
        }
        else -> {
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            LocalDateTime.parse(this, pattern)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatToReadableDate(): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'a las' h:mm a", Locale("es", "ES"))
    val formattedDate = this.format(formatter)
    return formattedDate.split(" ").joinToString(" ") { word ->
        if (word.lowercase() !in listOf("de", "a", "las")) {
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } else {
            word
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatToDateOnly(): String {
    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    val formattedDate = this.format(formatter)
    return formattedDate.split(" ").joinToString(" ") { word ->
        if (word.lowercase() !in listOf("de")) {
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } else {
            word
        }
    }
}

fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
