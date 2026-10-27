package com.example.nexum_trabajador.ui.presenter.job_application.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Componente que muestra la navegación entre semanas, el paso actual,
 * las horas totales seleccionadas y la leyenda del calendario.
 *
 * Se adapta automáticamente a pantallas apaisadas (landscape) y verticales (portrait).
 */
@Composable
fun ScheduleWeekNavigator(
    isCompactLandscape: Boolean,
    weekStart: LocalDate,
    weekEnd: LocalDate,
    currentWeekOffset: Int,
    canGoBack: Boolean,
    totalHoursSelected: Int,
    onPrevWeek: () -> Unit,
    onNextWeek: () -> Unit,
    is24Hours: Boolean = false,
    modifier: Modifier = Modifier
) {
    val weekRangeFormatter = remember {
        DateTimeFormatter.ofPattern("d MMM", Locale("es", "CO"))
    }

    if (isCompactLandscape) {
        // Barra compacta horizontal única para modo apaisado
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Izquierda: Paso + chip de horas seleccionadas
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Paso 4 de 5",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                if (totalHoursSelected > 0) {
                    Surface(
                        color = ScheduleColors.PrimaryGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${totalHoursSelected}h",
                            style = MaterialTheme.typography.labelSmall,
                            color = ScheduleColors.PrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // Centro: Controles de navegación de semana
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    onClick = onPrevWeek,
                    enabled = canGoBack,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        Icons.Filled.ChevronLeft,
                        contentDescription = "Semana anterior",
                        tint = if (canGoBack) ScheduleColors.PrimaryGreen else Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "${weekStart.format(weekRangeFormatter)} – ${weekEnd.format(weekRangeFormatter)} ${weekEnd.year}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                IconButton(
                    onClick = onNextWeek,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = "Semana siguiente",
                        tint = ScheduleColors.PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Derecha: Leyenda compacta
            ScheduleLegend(isCompact = true, is24Hours = is24Hours)
        }
    } else {
        // Modo vertical: Paso, navegación y leyenda organizados en filas
        Column(modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Paso 4 de 5",
                    style = MaterialTheme.typography.labelMedium
                )
                if (totalHoursSelected > 0) {
                    Surface(
                        color = ScheduleColors.PrimaryGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "$totalHoursSelected h seleccionadas",
                            style = MaterialTheme.typography.labelMedium,
                            color = ScheduleColors.PrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Navegación de semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onPrevWeek,
                    enabled = canGoBack,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Filled.ChevronLeft,
                        contentDescription = "Semana anterior",
                        tint = if (canGoBack) ScheduleColors.PrimaryGreen else Color.LightGray
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${weekStart.format(weekRangeFormatter)} – ${weekEnd.format(weekRangeFormatter)} ${weekEnd.year}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    if (currentWeekOffset > 0) {
                        Text(
                            text = "Semana ${currentWeekOffset + 1}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
                IconButton(
                    onClick = onNextWeek,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = "Semana siguiente",
                        tint = ScheduleColors.PrimaryGreen
                    )
                }
            }

            // Leyenda de colores con scroll horizontal de seguridad para pantallas ultra pequeñas
            ScheduleLegend(
                isCompact = false,
                is24Hours = is24Hours,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}
