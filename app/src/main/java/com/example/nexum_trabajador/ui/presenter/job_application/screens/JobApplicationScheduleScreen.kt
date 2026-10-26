package com.example.nexum_trabajador.ui.presenter.job_application.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.data.job_application.remote.payload.req.TimeRangeReq
import com.example.nexum_trabajador.ui.presenter.job_application.JobApplicationViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// ─────────────────────────────────────────────────────────────────────────────
// ── Horario laboral configurable ──────────────────────────────────────────────
//    Cambia estos valores para definir el rango de horas laborables.
//    Horas fuera de este rango se mostrarán en rojo y no serán seleccionables.
//    WORK_START_HOUR : primera hora SELECCIONABLE (inclusive)  — ej: 7 = 7:00 AM
//    WORK_END_HOUR   : primera hora NO seleccionable (exclusive) — ej: 20 = 8:00 PM
// ─────────────────────────────────────────────────────────────────────────────
private const val WORK_START_HOUR = 7   // 7 AM
private const val WORK_END_HOUR   = 20  // 8 PM  (la franja 19:00–20:00 es la última)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationScheduleScreen(
    viewModel: JobApplicationViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Fecha de inicio del trabajo (Paso 1). Si no se ingresó, usamos hoy.
    val startDate: LocalDate = remember(state.scheduledDate) {
        if (state.scheduledDate.isNotBlank()) {
            try { LocalDate.parse(state.scheduledDate.substring(0, 10)) }
            catch (e: Exception) { LocalDate.now() }
        } else LocalDate.now()
    }

    // Primer lunes de la semana que contiene startDate
    val firstMonday: LocalDate = remember(startDate) {
        startDate.minusDays(((startDate.dayOfWeek.value - 1).toLong()).coerceAtLeast(0))
    }

    var currentWeekOffset by remember { mutableIntStateOf(0) }

    val weekStart: LocalDate by remember(currentWeekOffset, firstMonday) {
        derivedStateOf { firstMonday.plusWeeks(currentWeekOffset.toLong()) }
    }
    val weekEnd: LocalDate by remember(weekStart) {
        derivedStateOf { weekStart.plusDays(6) }
    }

    // ── Hora actual: se actualiza cada 30 segundos para el indicador en tiempo real
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    val today: LocalDate = remember { LocalDate.now() }
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000L)
            currentTime = LocalTime.now()
        }
    }

    // gridState acumulativo — persiste entre semanas
    var gridState by remember { mutableStateOf(emptyMap<Pair<LocalDate, Int>, Boolean>()) }

    // ── Colores ───────────────────────────────────────────────────────────────
    val primaryGreen        = Color(0xFF4A7D29)
    val colorPast           = Color(0xFFF0F0F0)   // gris claro — fecha/hora pasada o bloqueada
    val colorNonWorking     = Color(0xFFFFEBEE)   // rojo tenue — fuera de horario laboral
    val colorTodayBg        = Color(0xFFF1F8E9)   // verde tenue — hoy (hora libre)
    val colorTimeline       = Color(0xFFE53935)   // rojo vivo  — línea de hora actual
    val colorSelected       = primaryGreen

    val dayLetters          = listOf("L", "M", "X", "J", "V", "S", "D")
    val weekRangeFormatter  = DateTimeFormatter.ofPattern("d MMM", Locale("es", "CO"))
    val totalHoursSelected  = gridState.count { it.value }

    // ── Estados de scroll compartidos ─────────────────────────────────────────
    val verticalScrollState:   ScrollState = rememberScrollState()
    val horizontalScrollState: ScrollState = rememberScrollState()

    val cellHeight:   Dp = 40.dp
    val cellWidth:    Dp = 44.dp
    val hourColWidth: Dp = 50.dp

    // Posición vertical de la línea de hora actual desde el tope de la grilla
    val timelineDp: Dp by remember(currentTime) {
        derivedStateOf {
            cellHeight * (currentTime.hour + currentTime.minute / 60f)
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────────
    fun isNonWorking(hour: Int) = hour < WORK_START_HOUR || hour >= WORK_END_HOUR

    /** Devuelve true si la celda (date, hour) NO debe ser seleccionable */
    fun isBlocked(date: LocalDate, hour: Int): Boolean {
        if (date < startDate)           return true  // Antes de la fecha de inicio del trabajo
        if (date < today)               return true  // Fecha pasada
        if (date == today && hour <= currentTime.hour) return true  // Hora ya pasada hoy
        if (isNonWorking(hour))         return true  // Fuera de horario laboral
        return false
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Calendario de trabajo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                    }
                },
                windowInsets = WindowInsets(top = (statusBarHeight - 12.dp).coerceAtLeast(0.dp)),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // ── Paso + horas seleccionadas ─────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Paso 4 de 5", style = MaterialTheme.typography.labelMedium)
                if (totalHoursSelected > 0) {
                    Text(
                        "$totalHoursSelected h seleccionadas",
                        style = MaterialTheme.typography.labelMedium,
                        color = primaryGreen, fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // ── Navegación de semana ──────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val canGoBack = currentWeekOffset > 0
                IconButton(onClick = { if (canGoBack) currentWeekOffset-- }, enabled = canGoBack) {
                    Icon(Icons.Filled.ChevronLeft, "Semana anterior",
                        tint = if (canGoBack) primaryGreen else Color.LightGray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${weekStart.format(weekRangeFormatter)} – ${weekEnd.format(weekRangeFormatter)} ${weekEnd.year}",
                        fontWeight = FontWeight.Bold, fontSize = 13.sp
                    )
                    if (currentWeekOffset > 0) {
                        Text("Semana ${currentWeekOffset + 1}", fontSize = 10.sp, color = Color.Gray)
                    }
                }
                IconButton(onClick = { currentWeekOffset++ }) {
                    Icon(Icons.Filled.ChevronRight, "Semana siguiente", tint = primaryGreen)
                }
            }

            // ── Leyenda de colores ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalendarLegendItem(colorNonWorking, Color(0xFFEF9A9A), "No laborable")
                CalendarLegendItem(colorPast,       Color(0xFFBDBDBD), "No disponible")
                CalendarLegendItem(colorSelected,   colorSelected,     "Seleccionado")
                // Línea de tiempo actual
                Box(modifier = Modifier.width(16.dp).height(2.dp).background(colorTimeline))
                Text("Ahora", fontSize = 10.sp, color = Color.Gray)
            }

            HorizontalDivider()

            // ── Grilla sincronizada ───────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {

                // Encabezados de días — FIJOS verticalmente, sincronizan scroll horizontal
                Row {
                    Box(modifier = Modifier.width(hourColWidth).height(46.dp)) // esquina vacía
                    Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
                        repeat(7) { dayIndex ->
                            val date   = weekStart.plusDays(dayIndex.toLong())
                            val isToday = date == today
                            val isPastDay = date < today || date < startDate
                            Column(
                                modifier = Modifier
                                    .width(cellWidth)
                                    .height(46.dp)
                                    .background(
                                        when {
                                            isToday  -> colorTodayBg
                                            isPastDay -> colorPast
                                            else     -> Color.Transparent
                                        }
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    dayLetters[dayIndex],
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = when { isToday -> primaryGreen; isPastDay -> Color.LightGray; else -> Color.DarkGray }
                                )
                                Text(
                                    date.dayOfMonth.toString(),
                                    fontSize = 10.sp,
                                    color = when { isToday -> primaryGreen; isPastDay -> Color.LightGray; else -> Color.Gray },
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

                // Cuerpo: scroll vertical compartido → horas y celdas se mueven juntos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(verticalScrollState)
                ) {
                    // Columna de etiquetas de hora (fija horizontalmente)
                    Column(modifier = Modifier.width(hourColWidth)) {
                        repeat(24) { hour ->
                            Box(
                                modifier = Modifier.height(cellHeight).fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(
                                    text = when {
                                        hour == 0  -> "12 am"; hour < 12  -> "$hour am"
                                        hour == 12 -> "12 pm"; else       -> "${hour - 12} pm"
                                    },
                                    fontSize = 10.sp,
                                    // Horas fuera de horario laboral se muestran en rojo
                                    color = if (isNonWorking(hour)) Color(0xFFE53935) else Color.DarkGray,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }
                    }

                    // Columnas de celdas — scroll horizontal compartido
                    Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
                        repeat(7) { dayIndex ->
                            val date    = weekStart.plusDays(dayIndex.toLong())
                            val isToday = date == today

                            // Box relativo: permite superponer la línea de hora actual
                            Box {
                                Column(modifier = Modifier.width(cellWidth)) {
                                    repeat(24) { hourIndex ->
                                        val key      = Pair(date, hourIndex)
                                        val blocked  = isBlocked(date, hourIndex)
                                        val nonWork  = isNonWorking(hourIndex)
                                        val selected = gridState[key] ?: false

                                        val bgColor = when {
                                            selected               -> colorSelected
                                            blocked && nonWork     -> colorNonWorking
                                            blocked                -> colorPast
                                            isToday                -> colorTodayBg
                                            else                   -> Color.Transparent
                                        }

                                        Box(
                                            modifier = Modifier
                                                .height(cellHeight)
                                                .width(cellWidth)
                                                .border(0.5.dp, Color(0xFFE0E0E0))
                                                .background(bgColor)
                                                .then(
                                                    if (!blocked) {
                                                        Modifier.clickable {
                                                            val m = gridState.toMutableMap()
                                                            m[key] = !selected
                                                            gridState = m
                                                        }
                                                    } else Modifier
                                                )
                                        )
                                    }
                                }

                                // ── Indicador de hora actual (solo en columna de hoy) ──
                                if (isToday) {
                                    Box(
                                        modifier = Modifier
                                            .offset(y = timelineDp)
                                            .width(cellWidth)
                                            .height(2.dp)
                                            .background(colorTimeline)
                                    )
                                    // Círculo en el borde izquierdo de la línea
                                    Box(
                                        modifier = Modifier
                                            .offset(x = (-3).dp, y = timelineDp - 3.dp)
                                            .size(8.dp)
                                            .background(colorTimeline, RoundedCornerShape(50))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // ── Botón Siguiente ───────────────────────────────────────────
            Button(
                onClick = {
                    val schedules = mutableMapOf<String, MutableList<TimeRangeReq>>()
                    gridState.forEach { (key, isSelected) ->
                        if (isSelected) {
                            val dateStr = key.first.format(DateTimeFormatter.ISO_LOCAL_DATE)
                            val hour    = key.second
                            schedules.getOrPut(dateStr) { mutableListOf() }
                                .add(TimeRangeReq(
                                    startTime = String.format("%02d:00", hour),
                                    endTime   = String.format("%02d:00", hour + 1)
                                ))
                        }
                    }
                    viewModel.updateSchedule(schedules)
                    onNavigateNext()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
            ) {
                Text("Siguiente", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

/** Elemento de leyenda del calendario */
@Composable
private fun CalendarLegendItem(fill: Color, border: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .background(fill, RoundedCornerShape(2.dp))
                .border(0.5.dp, border, RoundedCornerShape(2.dp))
        )
        Text(label, fontSize = 9.sp, color = Color.Gray)
    }
}
