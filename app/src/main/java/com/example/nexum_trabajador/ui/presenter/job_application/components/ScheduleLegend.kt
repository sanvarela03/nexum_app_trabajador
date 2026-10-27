package com.example.nexum_trabajador.ui.presenter.job_application.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Colores estándar utilizados en el calendario de disponibilidad.
 */
object ScheduleColors {
    val PrimaryGreen          = Color(0xFF4A7D29)
    val NonWorking            = Color(0xFFFFEBEE)
    val NonWorkingBorder      = Color(0xFFEF9A9A)
    val PastOrBlocked         = Color(0xFFF0F0F0)
    val PastOrBlockedBorder   = Color(0xFFBDBDBD)
    val TodayBackground       = Color(0xFFF1F8E9)
    val Timeline              = Color(0xFFE53935)
    val CellBorder            = Color(0xFFE0E0E0)

    // Colores para la fecha y hora solicitada por el cliente
    val ClientRequested       = Color(0xFFFFF8E1) // Ámbar suave de fondo
    val ClientRequestedBorder = Color(0xFFFFB300) // Borde ámbar cálido
    val ClientRequestedAccent = Color(0xFFF57C00) // Naranja/Ámbar vibrante para badge e iconos
    val ClientRequestedText   = Color(0xFFB45309) // Texto legible
}

/**
 * Elemento individual de la leyenda del calendario.
 */
@Composable
fun CalendarLegendItem(
    fill: Color,
    border: Color,
    label: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 9.sp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .background(fill, RoundedCornerShape(2.dp))
                .border(0.5.dp, border, RoundedCornerShape(2.dp))
        )
        Text(
            text = label,
            fontSize = fontSize,
            color = Color.Gray
        )
    }
}

/**
 * Barra completa de leyenda del calendario, adaptable a modo compacto, estándar y servicio 24 horas.
 */
@Composable
fun ScheduleLegend(
    isCompact: Boolean,
    is24Hours: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(if (isCompact) 6.dp else 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!is24Hours) {
            CalendarLegendItem(
                fill = ScheduleColors.NonWorking,
                border = ScheduleColors.NonWorkingBorder,
                label = if (isCompact) "No lab." else "No laborable",
                fontSize = if (isCompact) 9.sp else 10.sp
            )
        } else {
            Surface(
                color = ScheduleColors.PrimaryGreen.copy(alpha = 0.12f),
                shape = RoundedCornerShape(3.dp)
            ) {
                Text(
                    text = "24 Horas",
                    fontSize = if (isCompact) 9.sp else 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = ScheduleColors.PrimaryGreen,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }
        CalendarLegendItem(
            fill = ScheduleColors.PastOrBlocked,
            border = ScheduleColors.PastOrBlockedBorder,
            label = if (isCompact) "No disp." else "No disponible",
            fontSize = if (isCompact) 9.sp else 10.sp
        )
        CalendarLegendItem(
            fill = ScheduleColors.PrimaryGreen,
            border = ScheduleColors.PrimaryGreen,
            label = if (isCompact) "Selec." else "Seleccionado",
            fontSize = if (isCompact) 9.sp else 10.sp
        )
        CalendarLegendItem(
            fill = ScheduleColors.ClientRequested,
            border = ScheduleColors.ClientRequestedBorder,
            label = if (isCompact) "Cliente" else "Sol. cliente",
            fontSize = if (isCompact) 9.sp else 10.sp
        )
        Box(
            modifier = Modifier
                .width(if (isCompact) 10.dp else 14.dp)
                .height(2.dp)
                .background(ScheduleColors.Timeline)
        )
        Text(
            text = "Ahora",
            fontSize = if (isCompact) 9.sp else 10.sp,
            color = Color.Gray
        )
    }
}
