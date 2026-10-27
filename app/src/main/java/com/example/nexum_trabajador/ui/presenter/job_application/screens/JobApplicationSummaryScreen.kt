package com.example.nexum_trabajador.ui.presenter.job_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddMaterialReq
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddTaskReq
import com.example.nexum_trabajador.data.job_application.remote.payload.req.TimeRangeReq
import com.example.nexum_trabajador.ui.presenter.job_application.JobApplicationState
import com.example.nexum_trabajador.ui.presenter.job_application.JobApplicationViewModel
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme
import java.util.Locale
import com.example.nexum_trabajador.ui.presenter.job_application.components.JobApplicationStatusDialog

@Composable
fun JobApplicationSummaryScreen(
    viewModel: JobApplicationViewModel,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    JobApplicationSummaryScreenContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onSubmit = { viewModel.submitJobApplication() },
        onAcceptSuccess = {
            viewModel.resetSubmissionState()
            onSuccess()
        },
        onRetry = {
            viewModel.submitJobApplication(isRetry = true)
        },
        onDismissError = {
            viewModel.dismissError()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JobApplicationSummaryScreenContent(
    state: JobApplicationState,
    onNavigateBack: () -> Unit,
    onSubmit: () -> Unit,
    onAcceptSuccess: () -> Unit = {},
    onRetry: () -> Unit = {},
    onDismissError: () -> Unit = {}
) {
    val primaryGreen = Color(0xFF4A7D29)

    Scaffold(
        topBar = {
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Resumen de tu propuesta",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                windowInsets = WindowInsets(
                    top = (statusBarHeight - 12.dp).coerceAtLeast(0.dp)
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Header info
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Paso 5 de 5",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = primaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Revisa los detalles",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Asegúrate de que toda la información ingresada coincida con los requerimientos del cliente.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
            }

            // 1. Costos y Garantías Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Costos y Estimación",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = primaryGreen
                    )
                    
                    HorizontalDivider(color = Color(0xFFEEEEEE))

                    // Mano de Obra
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(18.dp))
                            Text("Mano de obra", fontSize = 14.sp, color = Color.DarkGray)
                        }
                        Text(
                            text = "$ ${state.laborCost.toLong()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }

                    // Garantía
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(18.dp))
                            Text("Garantía", fontSize = 14.sp, color = Color.DarkGray)
                        }
                        Text(
                            text = "${state.workWarrantyInDays} días",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }

                    // Tiempo Estimado
                    val hoursStr = if (state.estimatedTimeInMinutes >= 60.0) {
                        val hours = (state.estimatedTimeInMinutes / 60.0).toInt()
                        val mins = (state.estimatedTimeInMinutes % 60.0).toInt()
                        if (mins > 0) "${hours}h ${mins}m" else "${hours}h"
                    } else {
                        "${state.estimatedTimeInMinutes.toInt()}m"
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(18.dp))
                            Text("Tiempo estimado", fontSize = 14.sp, color = Color.DarkGray)
                        }
                        Text(
                            text = hoursStr,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            // 2. Propuesta Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Description, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(18.dp))
                        Text(
                            text = "Propuesta Escrita",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = primaryGreen
                        )
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Text(
                        text = state.proposalText,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )
                }
            }

            // 3. Tareas Card (Checklist style)
            if (state.tasks.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Tareas Planificadas (${state.tasks.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = primaryGreen
                        )
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        state.tasks.forEach { task ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = primaryGreen,
                                    modifier = Modifier.size(18.dp).padding(top = 2.dp)
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = task.taskName,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                    if (task.taskDescription.isNotBlank()) {
                                        Text(
                                            text = task.taskDescription,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Materiales Card
            if (state.materials.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Presupuesto de Materiales (${state.materials.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = primaryGreen
                        )
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        state.materials.forEach { mat ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = "${mat.quantity}x ${mat.name}",
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                                Text(
                                    text = "$ ${(mat.unitPrice * mat.quantity).toLong()}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }

            // 5. Horario Seleccionado Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(20.dp))
                    Text(
                        text = "Horas reservadas en calendario:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    val totalSlots = state.workSchedules.values.flatten().size
                    Text(
                        text = "$totalSlots horas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = primaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (state.errorMessage.isNotEmpty()) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !state.isSubmitting,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Enviar Propuesta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    JobApplicationStatusDialog(
        isSubmitting = state.isSubmitting,
        isSuccess = state.isSuccess,
        errorMessage = state.errorMessage,
        retryCount = state.retryCount,
        maxRetries = state.maxRetries,
        onAcceptSuccess = onAcceptSuccess,
        onRetry = onRetry,
        onDismissError = onDismissError,
        primaryColor = primaryGreen
    )
}

@Preview(showBackground = true)
@Composable
private fun JobApplicationSummaryScreenPreview() {
    val mockState = JobApplicationState(
        laborCost = 150000.0,
        workWarrantyInDays = 30,
        estimatedTimeInMinutes = 120.0,
        proposalText = "Esta es una propuesta detallada para la reparación de la fuga de agua en la cocina, incluyendo materiales de alta calidad y garantía extendida.",
        tasks = listOf(
            AddTaskReq("Desmontaje de tubería", "Se retirará la tubería dañada con cuidado."),
            AddTaskReq("Instalación de repuesto", "Se instalará una nueva tubería de cobre."),
            AddTaskReq("Pruebas de presión", "")
        ),
        materials = listOf(
            AddMaterialReq("Tubo Cobre 1/2", "3 metros", "Insumo", 1, 45000.0),
            AddMaterialReq("Soldadura estaño", "Rollo pequeño", "Consumible", 1, 12000.0)
        ),
        workSchedules = mapOf(
            "2024-05-20" to listOf(
                TimeRangeReq("08:00", "09:00"),
                TimeRangeReq("09:00", "10:00")
            )
        )
    )

    Nexum_trabajadorTheme {
        JobApplicationSummaryScreenContent(
            state = mockState,
            onNavigateBack = {},
            onSubmit = {}
        )
    }
}
