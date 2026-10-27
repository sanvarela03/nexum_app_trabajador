package com.example.nexum_trabajador.ui.presenter.job_application.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Representa los datos parseados de la fecha y hora solicitada por el cliente.
 */
data class ClientRequestedSchedule(
    val rawIso: String,
    val date: LocalDate,
    val hour: Int,
    val formattedDate: String,
    val formattedTime: String
)

/**
 * Utilidad para parsear el string ISO proveniente del backend (ej. "2026-10-27T10:00:00Z").
 */
fun parseClientRequestedDate(isoString: String?): ClientRequestedSchedule? {
    if (isoString.isNullOrBlank()) return null
    return try {
        val odt = try {
            OffsetDateTime.parse(isoString)
        } catch (_: Exception) {
            LocalDateTime.parse(isoString).atZone(ZoneId.systemDefault()).toOffsetDateTime()
        }
        val date = odt.toLocalDate()
        val hour = odt.hour
        val dateFormat = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es", "CO"))
        val timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        ClientRequestedSchedule(
            rawIso = isoString,
            date = date,
            hour = hour,
            formattedDate = date.format(dateFormat).replaceFirstChar { it.uppercase() },
            formattedTime = odt.format(timeFormat)
        )
    } catch (_: Exception) {
        try {
            val localDate = LocalDate.parse(isoString.substring(0, 10))
            ClientRequestedSchedule(
                rawIso = isoString,
                date = localDate,
                hour = 8,
                formattedDate = localDate.format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es", "CO"))).replaceFirstChar { it.uppercase() },
                formattedTime = "Día solicitado"
            )
        } catch (_: Exception) {
            null
        }
    }
}

/**
 * Tarjeta indicadora no invasiva que se despliega al hacer clic sobre la celda
 * o el encabezado del día solicitado por el cliente.
 */
@Composable
fun ClientRequestedIndicatorCard(
    schedule: ClientRequestedSchedule,
    isSelected: Boolean,
    onToggleSelect: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = ScheduleColors.ClientRequested
        ),
        border = BorderStroke(1.dp, ScheduleColors.ClientRequestedBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono destacado
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(ScheduleColors.ClientRequestedAccent.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = ScheduleColors.ClientRequestedAccent,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Información de la cita solicitada
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Horario preferido por el cliente",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = ScheduleColors.ClientRequestedText
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${schedule.formattedDate} • ${schedule.formattedTime}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Botón rápido de acción para seleccionar/deseleccionar
            OutlinedButton(
                onClick = onToggleSelect,
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) ScheduleColors.PrimaryGreen else Color.White,
                    contentColor = if (isSelected) Color.White else ScheduleColors.PrimaryGreen
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) ScheduleColors.PrimaryGreen else ScheduleColors.ClientRequestedBorder
                )
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "Agregada", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text(text = "Agregar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Botón cerrar indicador
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(28.dp)
                    .padding(start = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
