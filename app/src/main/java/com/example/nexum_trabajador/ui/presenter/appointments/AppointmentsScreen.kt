package com.example.nexum_trabajador.ui.presenter.appointments

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.EditCalendar
import com.example.nexum_trabajador.ui.components.UserAvatar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nexum_trabajador.data.contract.remote.payload.res.ContractRes
import java.text.NumberFormat
import java.util.Locale

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(AppointmentsEvent.ClearMessages)
        }
    }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(AppointmentsEvent.ClearMessages)
        }
    }

    // Modal dialog for entering the 6-digit meeting verification code
    if (state.showVerificationDialog && state.selectedContract != null) {
        VerificationCodeDialog(
            contract = state.selectedContract,
            code = state.verificationCodeInput,
            isVerifying = state.isVerifying,
            onCodeChange = { viewModel.onEvent(AppointmentsEvent.VerificationCodeChanged(it)) },
            onConfirm = { viewModel.onEvent(AppointmentsEvent.SubmitVerificationCode) },
            onDismiss = { viewModel.onEvent(AppointmentsEvent.DismissVerificationDialog) }
        )
    }

    // Confirmation dialog for marking work complete
    if (state.showCompletionDialog && state.selectedContract != null) {
        CompletionConfirmationDialog(
            contract = state.selectedContract,
            isCompleting = state.isCompleting,
            onConfirm = { viewModel.onEvent(AppointmentsEvent.ConfirmWorkCompleted) },
            onDismiss = { viewModel.onEvent(AppointmentsEvent.DismissCompletionDialog) }
        )
    }

    // Modal dialog for issuing the final labor invoice
    if (state.showInvoiceDialog && state.selectedContract != null) {
        GenerateInvoiceDialog(
            contract = state.selectedContract,
            amountInput = state.invoiceAmountInput,
            notesInput = state.invoiceNotesInput,
            isGenerating = state.isGeneratingInvoice,
            onAmountChange = { viewModel.onEvent(AppointmentsEvent.InvoiceAmountChanged(it)) },
            onNotesChange = { viewModel.onEvent(AppointmentsEvent.InvoiceNotesChanged(it)) },
            onConfirm = { viewModel.onEvent(AppointmentsEvent.SubmitInvoice) },
            onDismiss = { viewModel.onEvent(AppointmentsEvent.DismissInvoiceDialog) }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Mis Citas y Contratos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Verifica encuentros presenciales y gestiona contratos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { viewModel.onEvent(AppointmentsEvent.RefreshContracts) },
                    enabled = !state.isRefreshing
                ) {
                    if (state.isRefreshing) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            FilterSection(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { viewModel.onEvent(AppointmentsEvent.SelectFilter(it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            val filteredContracts = remember(state.contracts, state.selectedFilter) {
                when (state.selectedFilter) {
                    "SCHEDULED" -> state.contracts.filter {
                        it.currentStatus == "MEETING_SCHEDULED" || it.currentStatus == "CREATED"
                    }
                    "VERIFIED" -> state.contracts.filter {
                        it.currentStatus == "VERIFIED" || it.currentStatus == "INVOICED" || it.currentStatus == "ACTIVE"
                    }
                    "COMPLETED" -> state.contracts.filter {
                        it.currentStatus == "COMPLETED_BY_WORKER" || it.currentStatus == "COMPLETED"
                    }
                    else -> state.contracts
                }
            }

            if (state.isLoading && state.contracts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredContracts.isEmpty()) {
                EmptyContractsView(
                    filter = state.selectedFilter,
                    onRefresh = { viewModel.onEvent(AppointmentsEvent.RefreshContracts) }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredContracts, key = { it.id }) { contract ->
                        ContractCard(
                            contract = contract,
                            onVerifyClick = {
                                viewModel.onEvent(AppointmentsEvent.OpenVerificationDialog(contract))
                            },
                            onInvoiceClick = {
                                viewModel.onEvent(AppointmentsEvent.OpenInvoiceDialog(contract))
                            },
                            onCompleteClick = {
                                viewModel.onEvent(AppointmentsEvent.OpenCompletionDialog(contract))
                            }
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun FilterSection(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf(
        "ALL" to "Todos",
        "SCHEDULED" to "Por Verificar",
        "VERIFIED" to "En Ejecución",
        "COMPLETED" to "Finalizados"
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { (key, label) ->
            val isSelected = selectedFilter == key
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(key) },
                label = {
                    Text(
                        label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF1E7036),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF4B5563)
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = if (isSelected) Color(0xFF1E7036) else Color(0xFFE5E7EB)
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun ContractCard(
    contract: ContractRes,
    onVerifyClick: () -> Unit,
    onInvoiceClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Contract ID & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Contrato #${contract.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                StatusChip(status = contract.currentStatus)
            }

            // Datos del Cliente y Servicio (si existen)
            if (contract.clientName != null || contract.jobOfferTitle != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val clientName = contract.clientName ?: "Cliente"
                    UserAvatar(
                        imageUrl = contract.clientImageUrl,
                        name = clientName,
                        size = 44.dp
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = clientName,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = contract.jobOfferTitle ?: "Servicio contratado",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)

            // Panel de Información con diseño limpio
            Surface(
                color = Color(0xFFF9FAFB),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Monto acordado
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Monto acordado",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = currencyFormat.format(contract.agreedAmount),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                        }
                    }

                    // Fecha encuentro
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE3F2FD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = Color(0xFF1565C0),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Fecha de encuentro",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = formatMeetingDate(contract.meetingDate),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                        }
                    }

                    // Método de pago
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF3E8FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = Color(0xFF7E22CE),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Método de pago",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = contract.agreedFormOfPaymentName ?: "Plataforma",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF374151)
                            )
                        }
                    }
                }
            }

            // Nota de estado verificado
            if (contract.verifiedAt != null) {
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Encuentro validado presencialmente",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Botones de acción según el estado
            when (contract.currentStatus) {
                "CREATED", "MEETING_SCHEDULED" -> {
                    Button(
                        onClick = onVerifyClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E7036),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Verificar Código de Encuentro",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                "VERIFIED" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Encuentro validado presencialmente. Emite la factura final para que el cliente pague la mano de obra acordada.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                        Button(
                            onClick = onInvoiceClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE65100),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Emitir Factura Final (${currencyFormat.format(contract.agreedAmount)})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                "INVOICED" -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = Color(0xFFE65100),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Factura emitida por ${currencyFormat.format(contract.agreedAmount)}. Esperando pago de mano de obra del cliente para iniciar labores con garantía de custodia.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE65100),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                "ACTIVE" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFE0F2F1),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color(0xFF00695C),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Mano de obra pagada (fondos en custodia). Realiza las labores acordadas y marca como terminado al finalizar.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF00695C)
                                )
                            }
                        }
                        Button(
                            onClick = onCompleteClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1E7036),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Marcar Trabajo como Terminado",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                "COMPLETED_BY_WORKER" -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFEDE7F6),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "⏳",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Trabajo entregado. Esperando revisión y calificación del cliente.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF512DA8)
                            )
                        }
                    }
                }
                "COMPLETED" -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Contrato completado satisfactoriamente.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val (label, bg, text) = when (status) {
        "CREATED" -> Triple("Formalizado", Color(0xFFFFF8E1), Color(0xFFE65100))
        "MEETING_SCHEDULED" -> Triple("Cita Programada", Color(0xFFE3F2FD), Color(0xFF1565C0))
        "VERIFIED" -> Triple("Encuentro Verificado", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "INVOICED" -> Triple("Factura Emitida", Color(0xFFFFF3E0), Color(0xFFE65100))
        "ACTIVE" -> Triple("En Ejecución", Color(0xFFE0F2F1), Color(0xFF00695C))
        "COMPLETED_BY_WORKER" -> Triple("Terminado por ti", Color(0xFFEDE7F6), Color(0xFF512DA8))
        "COMPLETED" -> Triple("Completado", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "DISPUTED" -> Triple("En Disputa", Color(0xFFFFEBEE), Color(0xFFC62828))
        else -> Triple(status, Color(0xFFF3F4F6), Color(0xFF4B5563))
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, text.copy(alpha = 0.25f))
    ) {
        Text(
            text = label,
            color = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun VerificationCodeDialog(
    contract: ContractRes,
    code: String,
    isVerifying: Boolean,
    onCodeChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isVerifying) onDismiss() },
        icon = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            Text(
                text = "Verificar Llegada",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Pide al cliente el código de 6 dígitos que aparece en su pantalla de seguimiento para confirmar que estás en el lugar acordado.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = onCodeChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "000000",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 26.sp,
                            letterSpacing = 8.sp,
                            color = Color.LightGray
                        )
                    },
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 8.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(
                    text = "Contrato #${contract.id} • ${contract.meetingDate ?: "Hoy"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = code.length == 6 && !isVerifying,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isVerifying) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Validar Código", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            if (!isVerifying) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

@Composable
private fun CompletionConfirmationDialog(
    contract: ContractRes,
    isCompleting: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isCompleting) onDismiss() },
        icon = {
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(40.dp)
            )
        },
        title = {
            Text(
                text = "¿Terminaste el trabajo?",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Al marcar el trabajo como finalizado en el Contrato #${contract.id}, se notificará al cliente para que valide la entrega del servicio y proceda a calificar tu atención.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isCompleting,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                if (isCompleting) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                } else {
                    Text("Sí, Marcar Terminado", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            if (!isCompleting) {
                TextButton(onClick = onDismiss) {
                    Text("Volver")
                }
            }
        }
    )
}

@Composable
private fun GenerateInvoiceDialog(
    contract: ContractRes,
    amountInput: String,
    notesInput: String,
    isGenerating: Boolean,
    onAmountChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }
    }

    AlertDialog(
        onDismissRequest = { if (!isGenerating) onDismiss() },
        icon = {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = Color(0xFFE65100),
                modifier = Modifier.size(40.dp)
            )
        },
        title = {
            Text(
                text = "Emitir Factura de Mano de Obra",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Contrato #${contract.id} • ${contract.clientName ?: "Cliente"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Confirma el valor final que cobrarás por la mano de obra del trabajo pactado:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF374151)
                )

                OutlinedTextField(
                    value = amountInput,
                    onValueChange = onAmountChange,
                    label = { Text("Monto Mano de Obra ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    singleLine = true,
                    enabled = !isGenerating,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notesInput,
                    onValueChange = onNotesChange,
                    label = { Text("Detalle o notas de la factura") },
                    minLines = 2,
                    maxLines = 3,
                    enabled = !isGenerating,
                    modifier = Modifier.fillMaxWidth()
                )

                Surface(
                    color = Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🔒 Al emitir la factura, el cliente recibirá una notificación para realizar el pago. Los fondos quedarán retenidos en custodia hasta que finalices el trabajo.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFB26A00),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isGenerating && amountInput.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                } else {
                    Text("Emitir Factura", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            if (!isGenerating) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

@Composable
private fun EmptyContractsView(
    filter: String,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.EditCalendar,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = if (filter == "ALL") "No tienes citas o contratos aún"
                       else "No hay contratos en esta sección",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Cuando un cliente acepte tu postulación y pague la tarifa de seguridad, verás el contrato aquí.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            OutlinedButton(
                onClick = onRefresh,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Actualizar")
            }
        }
    }
}

private fun formatMeetingDate(dateStr: String?): String {
    if (dateStr.isNullOrBlank()) return "Por acordar en el chat"
    return try {
        val parsed = java.time.LocalDate.parse(dateStr)
        val formatter = java.time.format.DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", java.util.Locale("es", "CO"))
        parsed.format(formatter)
    } catch (e: Exception) {
        dateStr
    }
}

