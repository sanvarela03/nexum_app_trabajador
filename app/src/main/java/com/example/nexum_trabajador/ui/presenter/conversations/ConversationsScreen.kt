package com.example.nexum_trabajador.ui.presenter.conversations

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.example.nexum_trabajador.domain.model.Conversation
import com.example.nexum_trabajador.domain.model.Profile
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 2.4
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConversationsScreen(
    currentUserId: String,
    onConversationClick: (conversationId: String, otherUserId: String, otherUserRole: String) -> Unit,
    viewModel: ConversationsViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations.collectAsState()
    val profiles by viewModel.profiles.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val unreadCount by viewModel.unreadCount.collectAsState()
    
    val showNewChatDialog by viewModel.showNewChatDialog.collectAsState()
    val isSearchingProfiles by viewModel.isSearchingProfiles.collectAsState()
    
    Nexum_trabajadorTheme {
        ConversationsScreenContent(
            currentUserId = currentUserId,
            conversations = conversations,
            profiles = profiles,
            uiState = uiState,
            unreadCount = unreadCount,
            onRefresh = { viewModel.loadConversations(refresh = true) },
            onUpdateUnread = { viewModel.loadUnreadCount() },
            onConversationClick = onConversationClick,
            onNewChatClick = { viewModel.onOpenNewChatDialog() }
        )
        
        if (showNewChatDialog) {
            NewChatDialog(
                profiles = profiles.filter { it.id.toString() != currentUserId },
                isLoading = isSearchingProfiles,
                onDismiss = { viewModel.onCloseNewChatDialog() },
                onUserSelected = { profile ->
                    viewModel.onCloseNewChatDialog()
                    
                    val existingConversation = conversations.find { conv ->
                        conv.participantIds.contains(profile.id.toString())
                    }

                    if (existingConversation != null) {
                        onConversationClick(
                            existingConversation.id,
                            profile.id.toString(),
                            "CLIENTE"
                        )
                    } else {
                        onConversationClick("new", profile.id.toString(), "CLIENTE")
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationsScreenContent(
    currentUserId: String,
    conversations: List<Conversation>,
    profiles: List<Profile>,
    uiState: ConversationsUiState,
    unreadCount: Long,
    onRefresh: () -> Unit,
    onUpdateUnread: () -> Unit,
    onConversationClick: (String, String, String) -> Unit,
    onNewChatClick: () -> Unit
) {
    val isRefreshing = uiState is ConversationsUiState.Refreshing

    LaunchedEffect(Unit) {
        onUpdateUnread()
        onRefresh() // Also refresh conversations to clear the UI count on individual items
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    title = { Text("Mensajes") },
                    actions = {
                        IconButton(onClick = onUpdateUnread) {
                            Icon(Icons.Default.Chat, "Actualizar")
                        }
                    }
                )
                if (unreadCount > 0) {
                    Text(
                        text = "$unreadCount no leídos",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewChatClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.AddComment,
                    contentDescription = "Nuevo mensaje"
                )
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = onRefresh,
            modifier = Modifier.padding(paddingValues)
        ) {
            when (uiState) {
                is ConversationsUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ConversationsUiState.Error -> {
                    ErrorMessage(
                        message = uiState.message,
                        onRetry = onRefresh
                    )
                }

                else -> {
                    if (conversations.isEmpty()) {
                        EmptyState()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(items = conversations, key = { it.id }) { conversation ->
                                val otherUserId = conversation.participantIds
                                    .firstOrNull { it != currentUserId }

                                val otherUserProfile = profiles.find {
                                    it.id.toString() == otherUserId
                                }

                                ConversationItem(
                                    conversation = conversation,
                                    profile = otherUserProfile,
                                    currentUserId = currentUserId,
                                    onClick = {
                                        if (otherUserId != null) {
                                            val otherUserRole = conversation.participantRoles.first()
                                            onConversationClick(
                                                conversation.id,
                                                otherUserId,
                                                otherUserRole
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun NewChatDialog(
    profiles: List<Profile>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onUserSelected: (Profile) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Conversación") },
        text = {
            Box(modifier = Modifier.height(400.dp).fillMaxWidth()) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    LazyColumn {
                        items(profiles) { profile ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onUserSelected(profile) }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                com.example.nexum_trabajador.ui.components.UserAvatar(
                                    imageUrl = profile.imgUrl,
                                    name = "${profile.firstName} ${profile.lastName}",
                                    size = 40.dp
                                )
                                Spacer(Modifier.width(12.dp))
                                Text("${profile.firstName} ${profile.lastName}")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay conversaciones aún")
    }
}

@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = Color.Red)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ConversationItem(
    conversation: Conversation,
    profile: Profile?,
    currentUserId: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            com.example.nexum_trabajador.ui.components.UserAvatar(
                imageUrl = profile?.imgUrl,
                name = profile?.let { "${it.firstName} ${it.lastName}" },
                size = 50.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = profile?.let { "${it.firstName} ${it.lastName}" } ?: "Usuario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                    )
                    conversation.lastMessageAt?.let {
                        Text(
                            text = formatDate(it),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = conversation.lastMessageContent ?: "No hay mensajes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (conversation.unreadCount > 0) Color.Black else Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (conversation.unreadCount > 0) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = conversation.unreadCount.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val now = LocalDateTime.now()

        // Patrón para 12 horas con AM/PM
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

        when {
            dateTime.toLocalDate() == now.toLocalDate() -> {
                dateTime.format(timeFormatter)
            }
            dateTime.toLocalDate() == now.minusDays(1).toLocalDate() -> {
                "Ayer"
            }
            dateTime.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) ==
                now.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) -> {
                dateTime.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))
            }
            else -> {
                dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
            }
        }
    } catch (e: Exception) {
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ConversationsScreenPreview() {
    val currentUserId = "1"
    val mockProfiles = listOf(
        Profile(
            id = 2L,
            firstName = "Juan",
            lastName = "Pérez",
            username = "juanp",
            email = "juan@example.com",
            phone = "123456789",
            imgUrl = "",
            dateJoined = "2024-01-01T10:00:00Z",
            lastLogin = "2024-01-01T10:00:00Z",
            isEnabled = true
        ),
        Profile(
            id = 3L,
            firstName = "María",
            lastName = "García",
            username = "mariag",
            email = "maria@example.com",
            phone = "987654321",
            imgUrl = "",
            dateJoined = "2024-01-01T10:00:00Z",
            lastLogin = "2024-01-01T10:00:00Z",
            isEnabled = true
        )
    )

    val mockConversations = listOf(
        Conversation(
            id = "conv1",
            participantIds = setOf("1", "2"),
            participantRoles = setOf("TRABAJADOR", "CLIENTE"),
            lastMessageContent = "Hola, ¿cuándo podrías pasar a revisar el trabajo?",
            lastMessageAt = Instant.now().toString(),
            createdAt = Instant.now().toString(),
            unreadCount = 2
        ),
        Conversation(
            id = "conv2",
            participantIds = setOf("1", "3"),
            participantRoles = setOf("TRABAJADOR", "CLIENTE"),
            lastMessageContent = "Presupuesto enviado. Quedo atento.",
            lastMessageAt = Instant.now().minusSeconds(3600).toString(),
            createdAt = Instant.now().toString(),
            unreadCount = 0
        )
    )

    Nexum_trabajadorTheme {
        ConversationsScreenContent(
            currentUserId = currentUserId,
            conversations = mockConversations,
            profiles = mockProfiles,
            uiState = ConversationsUiState.Success,
            unreadCount = 2,
            onRefresh = {},
            onUpdateUnread = {},
            onConversationClick = { _, _, _ -> },
            onNewChatClick = {}
        )
    }
}
