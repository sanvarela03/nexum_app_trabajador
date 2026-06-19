package com.example.nexum_trabajador.ui.presenter.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults.outlinedCardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import androidx.compose.material3.ExperimentalMaterial3Api

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
@Preview(showBackground = true)
fun NotificationsScreenContentPreview() {
    Nexum_trabajadorTheme() {

        NotificationsScreenContent(
            notifications = listOf(
                NotificationEntity(
                    id = 1,
                    title = "Notification 1",
                    body = "Notification 1 body",
                    createdAt = "2021-01-01"
                ),
                NotificationEntity(
                    id = 2,
                    title = "Notification 2",
                    body = "Notification 2 body",
                    createdAt = "2021-01-01"
                )
            )
        )
    }
}

@Composable
private fun NotificationsScreenContent(
    notifications: List<NotificationEntity>,
    onDelete: (Int) -> Unit = {},
    onClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(notifications) { notification: NotificationEntity ->
                NotificationItem(notification, onDelete, onClick)
                Spacer(modifier = Modifier.height(10.dp))
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
            modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier.size(60.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = {
                        it.signature(ObjectKey(notification.id ?: 0))
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.jobOfferTitle ?: "Nueva Oferta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val dateObj = try {
                        notification.requiredDate?.let { OffsetDateTime.parse(it) }
                    } catch (e: Exception) { null }

                    val dateStr = dateObj?.format(DateTimeFormatter.ofPattern("MMMM d", Locale("es", "ES")))?.replaceFirstChar { it.uppercase() } ?: ""
                    val timeStr = dateObj?.format(DateTimeFormatter.ofPattern("h:mm a"))?.lowercase() ?: ""

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Date", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = dateStr, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Schedule, contentDescription = "Time", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = timeStr, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Location", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = notification.locationName ?: "Cerca de ti", fontSize = 14.sp)
                    }

                    if (!notification.categoryName.isNullOrBlank() || !notification.serviceType.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            notification.categoryName?.takeIf { it.isNotBlank() }?.let {
                                AssistChip(
                                    onClick = { },
                                    label = { Text(it, fontSize = 12.sp) }
                                )
                            }
                            notification.serviceType?.takeIf { it.isNotBlank() }?.let {
                                AssistChip(
                                    onClick = { },
                                    label = { Text(it, fontSize = 12.sp) }
                                )
                            }
                        }
                    }
                }

                // Delete Button
                IconButton(onClick = { onDelete(notification.id ?: -1) }) {
                    Icon(Icons.Filled.Close, contentDescription = "Delete")
                }
            }
        }
    } else {
        // Default Notification UI
        OutlinedCard(
            colors = outlinedCardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(4f)
                    )
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onDelete(notification.id ?: -1) }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "")
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = notification.body)
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.weight(1f),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        text = notification.createdAt,
                        textAlign = TextAlign.Right
                    )
                }
            }
        }
    }
}
