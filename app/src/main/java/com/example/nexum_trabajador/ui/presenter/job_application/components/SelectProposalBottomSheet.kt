package com.example.nexum_trabajador.ui.presenter.job_application.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.domain.model.ReusableProposal

enum class ProposalFilterType {
    ALL, QUOTES, DRAFTS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProposalBottomSheet(
    proposals: List<ReusableProposal>,
    isLoading: Boolean,
    onSelectProposal: (ReusableProposal) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedFilter by remember { mutableStateOf(ProposalFilterType.ALL) }

    val primaryGreen = Color(0xFF4A7D29)

    val quoteCount = proposals.count { !it.isDraft }
    val draftCount = proposals.count { it.isDraft }

    val filteredProposals = remember(proposals, selectedFilter) {
        when (selectedFilter) {
            ProposalFilterType.ALL -> proposals
            ProposalFilterType.QUOTES -> proposals.filter { !it.isDraft }
            ProposalFilterType.DRAFTS -> proposals.filter { it.isDraft }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Cotizaciones y Borradores",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1E293B)
                        )
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cerrar",
                        tint = Color.Gray
                    )
                }
            }

            Text(
                text = "Reutiliza una propuesta previa o borrador para rellenar automáticamente los datos y modificarlos a tu gusto.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == ProposalFilterType.ALL,
                    onClick = { selectedFilter = ProposalFilterType.ALL },
                    label = { Text("Todas (${proposals.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = primaryGreen.copy(alpha = 0.15f),
                        selectedLabelColor = primaryGreen
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                FilterChip(
                    selected = selectedFilter == ProposalFilterType.QUOTES,
                    onClick = { selectedFilter = ProposalFilterType.QUOTES },
                    label = { Text("Cotizaciones ($quoteCount)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = primaryGreen.copy(alpha = 0.15f),
                        selectedLabelColor = primaryGreen
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                FilterChip(
                    selected = selectedFilter == ProposalFilterType.DRAFTS,
                    onClick = { selectedFilter = ProposalFilterType.DRAFTS },
                    label = { Text("Borradores ($draftCount)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = primaryGreen.copy(alpha = 0.15f),
                        selectedLabelColor = primaryGreen
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            HorizontalDivider(color = Color(0xFFE2E8F0))

            Spacer(modifier = Modifier.height(12.dp))

            // Body
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryGreen)
                }
            } else if (filteredProposals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Description,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(56.dp)
                        )
                        Text(
                            text = "No hay propuestas disponibles",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Las cotizaciones que envíes o guardes como borrador aparecerán aquí.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredProposals, key = { it.id }) { proposal ->
                        ProposalCard(
                            proposal = proposal,
                            primaryGreen = primaryGreen,
                            onSelect = { onSelectProposal(proposal) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProposalCard(
    proposal: ReusableProposal,
    primaryGreen: Color,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Badges & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (proposal.isDraft) {
                    Surface(
                        color = Color(0xFFFFF7ED),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Borrador",
                            color = Color(0xFFC2410C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Surface(
                        color = Color(0xFFF0FDF4),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Cotización enviada",
                            color = Color(0xFF15803D),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = proposal.dateFormatted,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            // Title
            Text(
                text = proposal.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Proposal Text preview
            if (proposal.proposalText.isNotBlank()) {
                Text(
                    text = proposal.proposalText,
                    fontSize = 13.sp,
                    color = Color(0xFF475569),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            // Stats row (Cost, Warranty, counts)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Labor Cost
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AttachMoney,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$ ${proposal.laborCost.toLong()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B)
                    )
                }

                // Warranty
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${proposal.workWarrantyInDays} días",
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                }

                // Items info
                val tasksCount = proposal.tasks.size
                val materialsCount = proposal.materials.size
                Text(
                    text = "$tasksCount tareas • $materialsCount mat.",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            // Action Button
            Button(
                onClick = onSelect,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryGreen,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Usar esta propuesta",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
