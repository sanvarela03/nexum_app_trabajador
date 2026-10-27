package com.example.nexum_trabajador.ui.presenter.job_application.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime

// ─────────────────────────────────────────────────────────────────────────────
// ── Horario laboral configurable ──────────────────────────────────────────────
//    WORK_START_HOUR : primera hora SELECCIONABLE (inclusive)  — ej: 7 = 7:00 AM
//    WORK_END_HOUR   : primera hora NO seleccionable (exclusive) — ej: 20 = 8:00 PM
//    Si is24Hours es true, no aplica esta restricción de horario.
// ─────────────────────────────────────────────────────────────────────────────
const val WORK_START_HOUR = 7   // 7 AM
const val WORK_END_HOUR   = 20  // 8 PM  (la franja 19:00–20:00 es la última)

fun isNonWorking(hour: Int, is24Hours: Boolean = false): Boolean {
    if (is24Hours) return false
    return hour < WORK_START_HOUR || hour >= WORK_END_HOUR
}

/** Devuelve true si la celda (date, hour) NO debe ser seleccionable */
@RequiresApi(Build.VERSION_CODES.O)
fun isBlocked(
    date: LocalDate,
    hour: Int,
    startDate: LocalDate,
    today: LocalDate,
    currentTime: LocalTime,
    is24Hours: Boolean = false
): Boolean {
    if (date < startDate) return true
    if (date < today) return true
    if (date == today && hour <= currentTime.hour) return true
    if (isNonWorking(hour, is24Hours)) return true
    return false
}

/**
 * Grilla 24x7 del calendario con sincronización de scroll vertical y horizontal,
 * celdas seleccionables, línea roja de tiempo real y marcado no invasivo
 * de la fecha y hora solicitada por el cliente. Soporta modo 24 horas.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleGrid(
    modifier: Modifier = Modifier,
    hourColWidth: Dp,
    cellWidth: Dp,
    cellHeight: Dp,
    weekStart: LocalDate,
    today: LocalDate,
    startDate: LocalDate,
    currentTime: LocalTime,
    gridState: Map<Pair<LocalDate, Int>, Boolean>,
    verticalScrollState: ScrollState,
    horizontalScrollState: ScrollState,
    onToggleCell: (LocalDate, Int) -> Unit,
    is24Hours: Boolean = false,
    clientRequestedDate: LocalDate? = null,
    clientRequestedHour: Int? = null,
    onClientCellClick: ((LocalDate, Int) -> Unit)? = null,
) {
    // Posición vertical de la línea de tiempo real desde el inicio de la grilla
    val timelineDp: Dp by remember(currentTime, cellHeight) {
        derivedStateOf {
            cellHeight * (currentTime.hour + currentTime.minute / 60f)
        }
    }

    // Auto-scroll inicial: si es 24h, busca la hora del cliente o la hora actual; sino, 6 AM
    val density = LocalDensity.current
    LaunchedEffect(Unit) {
        val targetHour = (clientRequestedHour?.minus(1) ?: (if (is24Hours) (currentTime.hour - 1).coerceAtLeast(0) else (WORK_START_HOUR - 1))).coerceIn(0, 23)
        val scrollPx = with(density) { (cellHeight * targetHour).roundToPx() }
        verticalScrollState.scrollTo(scrollPx)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(verticalScrollState)
    ) {
        // Columna de etiquetas de hora (0 am – 11 pm), fija horizontalmente
        Column(modifier = Modifier.width(hourColWidth)) {
            repeat(24) { hour ->
                val nonWorking = isNonWorking(hour, is24Hours)
                Box(
                    modifier = Modifier
                        .height(cellHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = when {
                            hour == 0  -> "12 am"
                            hour < 12  -> "$hour am"
                            hour == 12 -> "12 pm"
                            else       -> "${hour - 12} pm"
                        },
                        fontSize = if (hourColWidth < 40.dp) 9.sp else 10.sp,
                        color = if (nonWorking) Color(0xFFE53935) else Color.DarkGray,
                        modifier = Modifier.padding(end = 4.dp),
                        maxLines = 1
                    )
                }
            }
        }

        // Columnas de celdas horarias para los 7 días (sincronizan scroll con los encabezados)
        Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
            repeat(7) { dayIndex ->
                val date = weekStart.plusDays(dayIndex.toLong())
                val isToday = date == today

                Box {
                    Column(modifier = Modifier.width(cellWidth)) {
                        repeat(24) { hourIndex ->
                            val key = Pair(date, hourIndex)
                            val isClientRequested = clientRequestedDate != null &&
                                    clientRequestedHour != null &&
                                    date == clientRequestedDate &&
                                    hourIndex == clientRequestedHour

                            val blocked = isBlocked(date, hourIndex, startDate, today, currentTime, is24Hours)
                            val nonWork = isNonWorking(hourIndex, is24Hours)
                            val selected = gridState[key] ?: false

                            val bgColor = when {
                                selected               -> ScheduleColors.PrimaryGreen
                                isClientRequested      -> ScheduleColors.ClientRequested
                                blocked && nonWork     -> ScheduleColors.NonWorking
                                blocked                -> ScheduleColors.PastOrBlocked
                                isToday                -> ScheduleColors.TodayBackground
                                else                   -> Color.Transparent
                            }

                            val borderColor = if (isClientRequested) {
                                ScheduleColors.ClientRequestedBorder
                            } else {
                                ScheduleColors.CellBorder
                            }

                            val borderWidth = if (isClientRequested) 1.5.dp else 0.5.dp

                            Box(
                                modifier = Modifier
                                    .height(cellHeight)
                                    .width(cellWidth)
                                    .border(borderWidth, borderColor)
                                    .background(bgColor)
                                    .then(
                                        if (!blocked) {
                                            Modifier.clickable {
                                                onToggleCell(date, hourIndex)
                                                if (isClientRequested) {
                                                    onClientCellClick?.invoke(date, hourIndex)
                                                }
                                            }
                                        } else if (isClientRequested) {
                                            Modifier.clickable {
                                                onClientCellClick?.invoke(date, hourIndex)
                                            }
                                        } else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                // Indicador visual no invasivo de la solicitud del cliente dentro de la celda
                                if (isClientRequested) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(2.dp)
                                            .size(if (cellHeight < 36.dp) 12.dp else 14.dp)
                                            .background(
                                                if (selected) Color.White.copy(alpha = 0.9f) else ScheduleColors.ClientRequestedAccent,
                                                RoundedCornerShape(3.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Solicitado por cliente",
                                            tint = if (selected) ScheduleColors.PrimaryGreen else Color.White,
                                            modifier = Modifier.size(if (cellHeight < 36.dp) 9.dp else 10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Indicador de hora actual en tiempo real (solo en la columna de hoy)
                    if (isToday) {
                        Box(
                            modifier = Modifier
                                .offset(y = timelineDp)
                                .width(cellWidth)
                                .height(2.dp)
                                .background(ScheduleColors.Timeline)
                        )
                        Box(
                            modifier = Modifier
                                .offset(x = (-3).dp, y = timelineDp - 3.dp)
                                .size(8.dp)
                                .background(ScheduleColors.Timeline, RoundedCornerShape(50))
                        )
                    }
                }
            }
        }
    }
}
