package com.example.nexum_trabajador.ui.presenter.job_application.screens

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.data.job_application.remote.payload.req.TimeRangeReq
import com.example.nexum_trabajador.ui.presenter.job_application.JobApplicationViewModel
import com.example.nexum_trabajador.ui.presenter.job_application.components.*
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Contenedor con estado (Stateful Composable) para la pantalla de selección de horario.
 * Administra la conexión con el [JobApplicationViewModel], el estado temporal de la selección,
 * los efectos secundarios de tiempo real y la fecha solicitada por el cliente.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobApplicationScheduleScreen(
    viewModel: JobApplicationViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Parseo de la fecha y hora solicitada por el cliente
    val clientRequestedSchedule = remember(state.clientRequiredDate) {
        parseClientRequestedDate(state.clientRequiredDate)
    }

    val today: LocalDate = remember { LocalDate.now() }
    val startDate: LocalDate = remember(today) { today }

    // Primer lunes de la semana actual
    val firstMonday: LocalDate = remember(startDate) {
        startDate.minusDays(((startDate.dayOfWeek.value - 1).toLong()).coerceAtLeast(0))
    }

    // Si la fecha solicitada por el cliente está en una semana futura, iniciamos en esa semana
    val initialWeekOffset = remember(clientRequestedSchedule, firstMonday) {
        if (clientRequestedSchedule != null && clientRequestedSchedule.date >= firstMonday) {
            val clientMonday = clientRequestedSchedule.date.minusDays(
                ((clientRequestedSchedule.date.dayOfWeek.value - 1).toLong()).coerceAtLeast(0)
            )
            ChronoUnit.WEEKS.between(firstMonday, clientMonday).toInt().coerceAtLeast(0)
        } else {
            0
        }
    }

    var currentWeekOffset by androidx.compose.runtime.saveable.rememberSaveable {
        mutableIntStateOf(initialWeekOffset)
    }

    val weekStart: LocalDate by remember(currentWeekOffset, firstMonday) {
        derivedStateOf { firstMonday.plusWeeks(currentWeekOffset.toLong()) }
    }
    val weekEnd: LocalDate by remember(weekStart) {
        derivedStateOf { weekStart.plusDays(6) }
    }

    // Hora actual: se actualiza cada 30 segundos para el indicador en tiempo real
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000L)
            currentTime = LocalTime.now()
        }
    }

    // Control para mostrar el indicador flotante de la cita del cliente (persiste al rotar)
    var showClientIndicator by androidx.compose.runtime.saveable.rememberSaveable {
        mutableStateOf(false)
    }

    // gridState derivado directamente del state del ViewModel (persiste al rotar la pantalla)
    val gridState by remember(state.workSchedules) {
        derivedStateOf {
            buildMap<Pair<LocalDate, Int>, Boolean> {
                state.workSchedules.forEach { (dateStr, ranges) ->
                    try {
                        val date = LocalDate.parse(dateStr)
                        ranges.forEach { range ->
                            val hour = range.startTime.substring(0, 2).toIntOrNull()
                            if (hour != null) {
                                put(Pair(date, hour), true)
                            }
                        }
                    } catch (_: Exception) {}
                }
            }
        }
    }

    val totalHoursSelected by remember(state.workSchedules) {
        derivedStateOf { state.workSchedules.values.sumOf { it.size } }
    }

    val onToggleCell: (LocalDate, Int) -> Unit = { date, hour ->
        viewModel.toggleScheduleSlot(date, hour)
    }

    val onSaveAndNext: () -> Unit = {
        onNavigateNext()
    }

    JobApplicationScheduleContent(
        startDate = startDate,
        today = today,
        currentTime = currentTime,
        weekStart = weekStart,
        weekEnd = weekEnd,
        currentWeekOffset = currentWeekOffset,
        canGoBackWeek = currentWeekOffset > 0,
        totalHoursSelected = totalHoursSelected,
        gridState = gridState,
        is24Hours = state.is24Hours,
        clientRequestedSchedule = clientRequestedSchedule,
        showClientIndicator = showClientIndicator,
        onShowClientIndicator = { showClientIndicator = true },
        onDismissClientIndicator = { showClientIndicator = false },
        onToggleCell = onToggleCell,
        onPrevWeek = { if (currentWeekOffset > 0) currentWeekOffset-- },
        onNextWeek = { currentWeekOffset++ },
        onSaveAndNext = onSaveAndNext,
        onNavigateBack = onNavigateBack
    )
}

/**
 * Componente visual sin estado (Stateless Presentation Composable) siguiendo el patrón State Hoisting.
 * Facilita previsualizaciones, pruebas unitarias y desacopla la UI de la lógica de negocio.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationScheduleContent(
    startDate: LocalDate,
    today: LocalDate,
    currentTime: LocalTime,
    weekStart: LocalDate,
    weekEnd: LocalDate,
    currentWeekOffset: Int,
    canGoBackWeek: Boolean,
    totalHoursSelected: Int,
    gridState: Map<Pair<LocalDate, Int>, Boolean>,
    is24Hours: Boolean = false,
    clientRequestedSchedule: ClientRequestedSchedule? = null,
    showClientIndicator: Boolean = false,
    onShowClientIndicator: () -> Unit = {},
    onDismissClientIndicator: () -> Unit = {},
    onToggleCell: (LocalDate, Int) -> Unit,
    onPrevWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onSaveAndNext: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Configuración responsiva para orientación y tamaño de pantalla
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val screenHeightDp = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val isCompactLandscape = isLandscape || screenHeightDp < 500.dp

    val hourColWidth: Dp = if (screenWidth < 360.dp) 38.dp else 42.dp
    val computedCellWidth = (screenWidth - hourColWidth) / 7
    val cellWidth: Dp = computedCellWidth.coerceAtLeast(38.dp)

    // Estados de scroll compartidos entre encabezados y cuerpo
    val verticalScrollState: ScrollState = rememberScrollState()
    val horizontalScrollState: ScrollState = rememberScrollState()

    val cellHeight: Dp = if (isCompactLandscape) 34.dp else 40.dp
    val headerHeight: Dp = if (isCompactLandscape) 48.dp else 52.dp

    Scaffold(
        modifier = modifier,
        topBar = {
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Calendario de trabajo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = if (isCompactLandscape) 16.sp else 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                actions = {
                    if (isCompactLandscape) {
                        Button(
                            onClick = onSaveAndNext,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .height(34.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = ScheduleColors.PrimaryGreen
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Siguiente",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ScheduleColors.PrimaryGreen
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = ScheduleColors.PrimaryGreen,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                },
                windowInsets = if (isCompactLandscape) {
                    WindowInsets(0.dp)
                } else {
                    WindowInsets(top = (statusBarHeight - 12.dp).coerceAtLeast(0.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScheduleColors.PrimaryGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Navegación de semana y leyendas
            ScheduleWeekNavigator(
                isCompactLandscape = isCompactLandscape,
                weekStart = weekStart,
                weekEnd = weekEnd,
                currentWeekOffset = currentWeekOffset,
                canGoBack = canGoBackWeek,
                totalHoursSelected = totalHoursSelected,
                is24Hours = is24Hours,
                onPrevWeek = onPrevWeek,
                onNextWeek = onNextWeek
            )

            // Indicador desplegable no invasivo de la fecha solicitada por el cliente
            AnimatedVisibility(
                visible = showClientIndicator && clientRequestedSchedule != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                clientRequestedSchedule?.let { schedule ->
                    val isSelected = gridState[Pair(schedule.date, schedule.hour)] == true
                    ClientRequestedIndicatorCard(
                        schedule = schedule,
                        isSelected = isSelected,
                        onToggleSelect = {
                            onToggleCell(schedule.date, schedule.hour)
                        },
                        onDismiss = onDismissClientIndicator
                    )
                }
            }

            HorizontalDivider()

            // Grilla de días y horas
            Column(modifier = Modifier.weight(1f)) {
                // Encabezados de días (fijos verticalmente, sincronizan scroll horizontal)
                ScheduleDayHeader(
                    hourColWidth = hourColWidth,
                    cellWidth = cellWidth,
                    headerHeight = headerHeight,
                    weekStart = weekStart,
                    today = today,
                    startDate = startDate,
                    isCompactLandscape = isCompactLandscape,
                    horizontalScrollState = horizontalScrollState,
                    clientRequestedDate = clientRequestedSchedule?.date,
                    onClientDateClick = onShowClientIndicator
                )

                HorizontalDivider()

                // Grilla horaria 24x7 sincronizada
                ScheduleGrid(
                    hourColWidth = hourColWidth,
                    cellWidth = cellWidth,
                    cellHeight = cellHeight,
                    weekStart = weekStart,
                    today = today,
                    startDate = startDate,
                    currentTime = currentTime,
                    gridState = gridState,
                    is24Hours = is24Hours,
                    verticalScrollState = verticalScrollState,
                    horizontalScrollState = horizontalScrollState,
                    onToggleCell = onToggleCell,
                    clientRequestedDate = clientRequestedSchedule?.date,
                    clientRequestedHour = clientRequestedSchedule?.hour,
                    onClientCellClick = { _, _ ->
                        onShowClientIndicator()
                    }
                )
            }

            // Botón Siguiente inferior (solo para modo vertical)
            if (!isCompactLandscape) {
                HorizontalDivider()
                Button(
                    onClick = onSaveAndNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ScheduleColors.PrimaryGreen
                    )
                ) {
                    Text(
                        text = "Siguiente",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun JobApplicationScheduleContentPreview() {
    val today = LocalDate.now()
    val previewSchedule = ClientRequestedSchedule(
        rawIso = "2026-10-27T10:00:00Z",
        date = today,
        hour = 10,
        formattedDate = "Martes 27 de octubre",
        formattedTime = "10:00 am"
    )

    JobApplicationScheduleContent(
        startDate = today,
        today = today,
        currentTime = LocalTime.of(10, 30),
        weekStart = today.minusDays(today.dayOfWeek.value.toLong() - 1),
        weekEnd = today.plusDays(7 - today.dayOfWeek.value.toLong()),
        currentWeekOffset = 0,
        canGoBackWeek = false,
        totalHoursSelected = 4,
        gridState = emptyMap(),
        is24Hours = false,
        clientRequestedSchedule = previewSchedule,
        showClientIndicator = true,
        onToggleCell = { _, _ -> },
        onPrevWeek = {},
        onNextWeek = {},
        onSaveAndNext = {},
        onNavigateBack = {}
    )
}
