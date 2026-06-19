package com.example.nexum_trabajador.ui.presenter.job_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.ui.presenter.job_application.JobApplicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationProposalScreen(
    viewModel: JobApplicationViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var proposalText by remember { mutableStateOf(state.proposalText) }
    var laborCost by remember { mutableStateOf(if (state.laborCost == 0.0) "" else state.laborCost.toString()) }
    var warrantyDays by remember { mutableStateOf(if (state.workWarrantyInDays == 0) "" else state.workWarrantyInDays.toString()) }

    val primaryGreen = Color(0xFF4A7D29)

    Scaffold(
        topBar = {
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Propuesta y Costos",
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
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header: Paso + Título de sección
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Paso 1 de 5",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = primaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Completa tu postulación",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Define los detalles del servicio que vas a ofrecer, tus honorarios y el período de garantía de tu labor.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
            }

            // Inputs Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Propuesta
                    OutlinedTextField(
                        value = proposalText,
                        onValueChange = { proposalText = it },
                        label = { Text("Texto de la propuesta") },
                        placeholder = { Text("Ej: Realizaré el mantenimiento preventivo y correctivo...") },
                        leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null, tint = primaryGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryGreen,
                            focusedLabelColor = primaryGreen,
                            cursorColor = primaryGreen
                        )
                    )

                    // Mano de Obra
                    OutlinedTextField(
                        value = laborCost,
                        onValueChange = { laborCost = it },
                        label = { Text("Costo de mano de obra") },
                        placeholder = { Text("0.00") },
                        leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = primaryGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryGreen,
                            focusedLabelColor = primaryGreen,
                            cursorColor = primaryGreen
                        )
                    )

                    // Garantía
                    OutlinedTextField(
                        value = warrantyDays,
                        onValueChange = { warrantyDays = it },
                        label = { Text("Garantía del trabajo (días)") },
                        placeholder = { Text("Ej: 30") },
                        leadingIcon = { Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = primaryGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryGreen,
                            focusedLabelColor = primaryGreen,
                            cursorColor = primaryGreen
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.updateProposalAndCost(
                        proposalText,
                        laborCost.toDoubleOrNull() ?: 0.0,
                        warrantyDays.toIntOrNull() ?: 0
                    )
                    onNavigateNext()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                enabled = proposalText.isNotBlank() && laborCost.isNotBlank() && warrantyDays.isNotBlank()
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
