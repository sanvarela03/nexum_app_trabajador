package com.example.nexum_trabajador.ui.presenter.job_application.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

private val DAY_LETTERS = listOf("L", "M", "X", "J", "V", "S", "D")
private val DAY_LETTERS_SHORT = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

/**
 * Encabezado superior con los días de la semana y sus fechas numéricas correspondientes.
 * Permanece fijo verticalmente mientras sincroniza su scroll horizontal con la grilla horaria.
 * Muestra un indicador visual si coincide con la fecha solicitada por el cliente.
 */
@Composable
fun ScheduleDayHeader(
    hourColWidth: Dp,
    cellWidth: Dp,
    headerHeight: Dp,
    weekStart: LocalDate,
    today: LocalDate,
    startDate: LocalDate,
    isCompactLandscape: Boolean,
    horizontalScrollState: ScrollState,
    clientRequestedDate: LocalDate? = null,
    onClientDateClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        // Esquina superior izquierda vacía alineada con la columna de horas
        Box(
            modifier = Modifier
                .width(hourColWidth)
                .height(headerHeight)
                .border(width = 0.5.dp, color = ScheduleColors.CellBorder)
        )

        // Fila desplazable con las 7 columnas de los días
        Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
            repeat(7) { dayIndex ->
                val date = weekStart.plusDays(dayIndex.toLong())
                val isToday = date == today
                val isPastDay = date < today || date < startDate
                val isClientDay = clientRequestedDate != null && date == clientRequestedDate

                val dayLabel = if (isCompactLandscape && cellWidth >= 60.dp) {
                    DAY_LETTERS_SHORT[dayIndex]
                } else {
                    DAY_LETTERS[dayIndex]
                }

                val headerBg = when {
                    isToday      -> ScheduleColors.TodayBackground
                    isClientDay  -> ScheduleColors.ClientRequested
                    isPastDay    -> ScheduleColors.PastOrBlocked
                    else         -> Color.White
                }

                val headerBorderColor = if (isClientDay && !isToday) {
                    ScheduleColors.ClientRequestedBorder
                } else {
                    ScheduleColors.CellBorder
                }

                Column(
                    modifier = Modifier
                        .width(cellWidth)
                        .height(headerHeight)
                        .border(
                            width = if (isClientDay) 1.dp else 0.5.dp,
                            color = headerBorderColor
                        )
                        .background(headerBg)
                        .then(
                            if (isClientDay && onClientDateClick != null) {
                                Modifier.clickable { onClientDateClick() }
                            } else Modifier
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Nombre/letra del día arriba con indicador si es día del cliente
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = dayLabel,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isCompactLandscape) 11.sp else 12.sp,
                            color = when {
                                isToday      -> ScheduleColors.PrimaryGreen
                                isClientDay  -> ScheduleColors.ClientRequestedText
                                isPastDay    -> Color(0xFF757575)
                                else         -> Color(0xFF374151)
                            },
                            lineHeight = if (isCompactLandscape) 12.sp else 14.sp
                        )
                        if (isClientDay) {
                            Spacer(modifier = Modifier.width(2.dp))
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(ScheduleColors.ClientRequestedAccent, CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Número del día centrado verticalmente debajo
                    if (isToday) {
                        Box(
                            modifier = Modifier
                                .size(if (isCompactLandscape) 20.dp else 24.dp)
                                .background(ScheduleColors.PrimaryGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                fontSize = if (isCompactLandscape) 11.sp else 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = if (isCompactLandscape) 11.sp else 12.sp
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.size(if (isCompactLandscape) 20.dp else 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                fontSize = if (isCompactLandscape) 12.sp else 13.sp,
                                fontWeight = if (isPastDay) FontWeight.Medium else FontWeight.Bold,
                                color = when {
                                    isClientDay  -> ScheduleColors.ClientRequestedText
                                    isPastDay    -> Color(0xFF9E9E9E)
                                    else         -> Color(0xFF111827)
                                },
                                lineHeight = if (isCompactLandscape) 12.sp else 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
