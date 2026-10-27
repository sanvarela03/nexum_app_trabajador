package com.example.nexum_trabajador.ui.presenter.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults.outlinedCardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class NotificationSortOrder(val label: String) {
    NEWEST("Más recientes"),
    OLDEST("Más antiguas")
}

fun getNotificationCategory(type: String?): String {
    return when (type) {
        "NEW_JOB_OFFER" -> "Ofertas"
        "CONTRACT_CREATED" -> "Contratos"
        "LABOR_PAID" -> "Pagos"
        "MEETING_SCHEDULED", "MEETING_VERIFIED" -> "Citas"
        "REVIEW_RECEIVED" -> "Calificaciones"
        "INVOICE_ISSUED" -> "Facturas"
        null, "" -> "General"
        else -> type.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }
    }
}

fun parseNotificationDateToMillis(dateStr: String?): Long {
    if (dateStr.isNullOrBlank()) return 0L
    val cleanDate = dateStr.trim()
        .replace("p. m.", "PM")
        .replace("a. m.", "AM")
        .replace("p.m.", "PM")
        .replace("a.m.", "AM")

    val formatters = listOf(
        DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a", Locale.US),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US),
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
    )
    for (formatter in formatters) {
        try {
            return LocalDateTime.parse(cleanDate, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } catch (_: Exception) {}
        try {
            return OffsetDateTime.parse(cleanDate, formatter)
                .toInstant()
                .toEpochMilli()
        } catch (_: Exception) {}
        try {
            return LocalDate.parse(cleanDate, formatter)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } catch (_: Exception) {}
    }
    return 0L
}

@Composable
fun NotificationsScreen(
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit = {}
) {
    val notifications =
        notificationViewModel.notifications.collectAsState(initial = emptyList()).value

    NotificationsScreenContent(
        notifications = notifications,
        onDelete = { notificationId ->
            notificationViewModel.onEvent(
                NotificationsEvent.DeleteBtnClick(
                    notificationId ?: -1
                )
            )
        },
        onClick = onNavigateToDetails
    )
}

@Composable
private fun NotificationsScreenContent(
    notifications: List<NotificationEntity>,
    onDelete: (Int) -> Unit = {},
    onClick: (String) -> Unit = {}
) {
    var selectedCategory by rememberSaveable { mutableStateOf("Todas") }
    var sortOrder by rememberSaveable { mutableStateOf(NotificationSortOrder.NEWEST) }

    // Calcular categorías disponibles dinámicamente según las notificaciones existentes
    val categoriesWithCounts = remember(notifications) {
        val counts = notifications
            .map { getNotificationCategory(it.type) }
            .groupingBy { it }
            .eachCount()

        val priorityOrder = listOf("Ofertas", "Contratos", "Pagos", "Citas", "Calificaciones", "Facturas", "General")
        val sortedKeys = counts.keys.sortedWith(
            compareBy(
                { priorityOrder.indexOf(it).takeIf { idx -> idx >= 0 } ?: 99 },
                { it }
            )
        )
        listOf("Todas" to notifications.size) + sortedKeys.map { it to (counts[it] ?: 0) }
    }

    // Si la categoría seleccionada ya no tiene elementos, volver a "Todas"
    LaunchedEffect(categoriesWithCounts) {
        if (selectedCategory != "Todas" && categoriesWithCounts.none { it.first == selectedCategory }) {
            selectedCategory = "Todas"
        }
    }

    // Filtrar y ordenar
    val sortedAndFilteredNotifications = remember(notifications, selectedCategory, sortOrder) {
        val filtered = if (selectedCategory == "Todas") {
            notifications
        } else {
            notifications.filter { getNotificationCategory(it.type) == selectedCategory }
        }

        when (sortOrder) {
            NotificationSortOrder.NEWEST -> {
                filtered.sortedWith(
                    compareByDescending<NotificationEntity> { parseNotificationDateToMillis(it.createdAt) }
                        .thenByDescending { it.id ?: 0 }
                )
            }
            NotificationSortOrder.OLDEST -> {
                filtered.sortedWith(
                    compareBy<NotificationEntity> { parseNotificationDateToMillis(it.createdAt) }
                        .thenBy { it.id ?: 0 }
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Barra Superior: Título/Contador + Control de Ordenamiento
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Notificaciones",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                if (notifications.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = Color(0xFF1E7036).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${sortedAndFilteredNotifications.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E7036),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Menú de Ordenamiento
            var sortMenuExpanded by remember { mutableStateOf(false) }
            Box {
                Surface(
                    onClick = { sortMenuExpanded = true },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF3F4F6),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.height(34.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Ordenar",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF1E7036)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = sortOrder.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF374151)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF6B7280)
                        )
                    }
                }

                DropdownMenu(
                    expanded = sortMenuExpanded,
                    onDismissRequest = { sortMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (sortOrder == NotificationSortOrder.NEWEST) Color(0xFF1E7036) else Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Más recientes",
                                    fontSize = 13.sp,
                                    fontWeight = if (sortOrder == NotificationSortOrder.NEWEST) FontWeight.Bold else FontWeight.Normal,
                                    color = if (sortOrder == NotificationSortOrder.NEWEST) Color(0xFF1E7036) else Color.Unspecified
                                )
                            }
                        },
                        onClick = {
                            sortOrder = NotificationSortOrder.NEWEST
                            sortMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (sortOrder == NotificationSortOrder.OLDEST) Color(0xFF1E7036) else Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Más antiguas",
                                    fontSize = 13.sp,
                                    fontWeight = if (sortOrder == NotificationSortOrder.OLDEST) FontWeight.Bold else FontWeight.Normal,
                                    color = if (sortOrder == NotificationSortOrder.OLDEST) Color(0xFF1E7036) else Color.Unspecified
                                )
                            }
                        },
                        onClick = {
                            sortOrder = NotificationSortOrder.OLDEST
                            sortMenuExpanded = false
                        }
                    )
                }
            }
        }

        // Filtros por Categoría (Chips)
        if (notifications.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoriesWithCounts.forEach { (category, count) ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = "$category ($count)",
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

        Spacer(modifier = Modifier.height(12.dp))

        // Contenido / Lista de Notificaciones
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tienes notificaciones",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Te avisaremos cuando recibas nuevas ofertas, cambios en contratos o pagos.",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (sortedAndFilteredNotifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No hay notificaciones de '$selectedCategory'",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { selectedCategory = "Todas" },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF1E7036)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF1E7036))
                    ) {
                        Text("Ver todas")
                    }
                }
            }
        } else {
            LazyColumn {
                items(
                    items = sortedAndFilteredNotifications,
                    key = { it.id ?: it.hashCode() }
                ) { notification: NotificationEntity ->
                    NotificationItem(notification, onDelete, onClick)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun NotificationItem(
    notification: NotificationEntity,
    onDelete: (Int) -> Unit,
    onClick: (String) -> Unit
) {
    if (notification.type == "NEW_JOB_OFFER") {
        OutlinedCard(
            onClick = { notification.jobOfferUuid?.let { onClick(it) } },
            colors = outlinedCardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Profile Image
                val clientName = notification.clientName ?: "User"
                val fallbackUrl = "https://ui-avatars.com/api/?name=${clientName}&background=random"
                val validImgUrl = notification.clientImageUrl?.takeIf {
                    it.isNotBlank() && it != "NaN" && it != "null" && it.startsWith("http") && !it.contains("localhost") && !it.contains("127.0.0.1")
                }

                GlideImage(
                    model = validImgUrl ?: fallbackUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = {
                        it.signature(ObjectKey(notification.id ?: 0))
                    }
                )

                Spacer(modifier = Modifier.width(14.dp))

                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.jobOfferTitle ?: "Nueva Oferta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val dateObj = try {
                        notification.requiredDate?.let { OffsetDateTime.parse(it) }
                    } catch (e: Exception) { null }

                    val dateStr = dateObj?.format(DateTimeFormatter.ofPattern("MMMM d", Locale("es", "ES")))?.replaceFirstChar { it.uppercase() } ?: ""
                    val timeStr = dateObj?.format(DateTimeFormatter.ofPattern("h:mm a"))?.lowercase() ?: ""

                    if (dateStr.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CalendarMonth, contentDescription = "Date", modifier = Modifier.size(15.dp), tint = Color(0xFF64748B))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = dateStr, fontSize = 13.sp, color = Color(0xFF475569))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    if (timeStr.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Schedule, contentDescription = "Time", modifier = Modifier.size(15.dp), tint = Color(0xFF64748B))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = timeStr, fontSize = 13.sp, color = Color(0xFF475569))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Location", modifier = Modifier.size(15.dp), tint = Color(0xFF64748B))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = notification.locationName ?: "Cerca de ti", fontSize = 13.sp, color = Color(0xFF475569))
                    }

                    if (!notification.categoryName.isNullOrBlank() || !notification.serviceType.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            notification.categoryName?.takeIf { it.isNotBlank() }?.let {
                                AssistChip(
                                    onClick = { },
                                    label = { Text(it, fontSize = 11.sp) }
                                )
                            }
                            notification.serviceType?.takeIf { it.isNotBlank() }?.let {
                                AssistChip(
                                    onClick = { },
                                    label = { Text(it, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }

                // Delete Button (Cerrar con Equis)
                IconButton(
                    onClick = { onDelete(notification.id ?: -1) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Eliminar notificación",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    } else {
        // Notificación Estándar / Otro tipo de notificación
        OutlinedCard(
            colors = outlinedCardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Icono temático según el tipo de notificación
                    val (icon, iconTint, iconBg) = when (notification.type) {
                        "CONTRACT_CREATED" -> Triple(Icons.AutoMirrored.Filled.Assignment, Color(0xFF0284C7), Color(0xFFE0F2FE))
                        "LABOR_PAID" -> Triple(Icons.Filled.Payments, Color(0xFF16A34A), Color(0xFFDCFCE7))
                        "MEETING_SCHEDULED", "MEETING_VERIFIED" -> Triple(Icons.Filled.CalendarMonth, Color(0xFFD97706), Color(0xFFFEF3C7))
                        "REVIEW_RECEIVED" -> Triple(Icons.Filled.Star, Color(0xFFCA8A04), Color(0xFFFEF9C3))
                        "INVOICE_ISSUED" -> Triple(Icons.Filled.Receipt, Color(0xFF7C3AED), Color(0xFFEDE9FE))
                        else -> Triple(Icons.Filled.Notifications, Color(0xFF1E7036), Color(0xFFE8F5E9))
                    }

                    Surface(
                        shape = CircleShape,
                        color = iconBg,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = iconTint
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = notification.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = notification.body,
                            fontSize = 13.sp,
                            color = Color(0xFF475569),
                            lineHeight = 18.sp
                        )
                    }

                    // Delete Button: Cambiado de bote de basura a la equis
                    IconButton(
                        onClick = { onDelete(notification.id ?: -1) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Eliminar notificación",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        text = notification.createdAt,
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NotificationsScreenContentPreview() {
    Nexum_trabajadorTheme {
        NotificationsScreenContent(
            notifications = listOf(
                NotificationEntity(
                    id = 1,
                    title = "Nueva Oferta de Trabajo",
                    body = "Se ha publicado una nueva oferta en tu zona",
                    createdAt = "2026-10-26 10:00:00 AM",
                    type = "NEW_JOB_OFFER",
                    jobOfferTitle = "Instalación de tuberías",
                    locationName = "Chapinero, Bogotá",
                    requiredDate = "2026-10-27T14:00:00Z",
                    categoryName = "Plomería",
                    serviceType = "Por hora"
                ),
                NotificationEntity(
                    id = 2,
                    title = "¡Pago de mano de obra recibido!",
                    body = "El cliente ha completado el pago del contrato #15.",
                    createdAt = "2026-10-26 09:30:00 AM",
                    type = "LABOR_PAID"
                ),
                NotificationEntity(
                    id = 3,
                    title = "¡Tu propuesta fue aceptada! 🎉",
                    body = "Juan Pérez ha aceptado tu cotización.",
                    createdAt = "2026-10-25 04:15:00 PM",
                    type = "CONTRACT_CREATED"
                )
            )
        )
    }
}
