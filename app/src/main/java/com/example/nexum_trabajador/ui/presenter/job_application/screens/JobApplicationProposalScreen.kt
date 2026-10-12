package com.example.nexum_trabajador.ui.presenter.job_application.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
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
import com.example.nexum_trabajador.ui.presenter.job_application.components.SelectProposalBottomSheet

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
    var showProposalPicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.proposalText, state.laborCost, state.workWarrantyInDays) {
        proposalText = state.proposalText
        laborCost = if (state.laborCost == 0.0) "" else state.laborCost.toString()
        warrantyDays = if (state.workWarrantyInDays == 0) "" else state.workWarrantyInDays.toString()
    }

    val primaryGreen = Color(0xFF4A7D29)

    if (showProposalPicker) {
        SelectProposalBottomSheet(
            proposals = state.reusableProposals,
            isLoading = state.isLoadingProposals,
            onSelectProposal = { proposal ->
                viewModel.applyReusableProposal(proposal)
                showProposalPicker = false
            },
            onDismiss = { showProposalPicker = false }
        )
    }

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
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
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

            // Botón para cargar cotización o borrador previo
            OutlinedCard(
                onClick = { showProposalPicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = primaryGreen.copy(alpha = 0.06f)
                ),
                border = BorderStroke(1.dp, primaryGreen.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        color = primaryGreen,
                        shape = CircleShape,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Usar cotización o borrador",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = primaryGreen
                            )
                        )
                        Text(
                            text = if (state.reusableProposals.isNotEmpty()) {
                                "${state.reusableProposals.size} disponibles para reutilizar"
                            } else {
                                "Ahorra tiempo reutilizando propuestas anteriores"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF4B5563),
                                fontSize = 12.sp
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
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

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
