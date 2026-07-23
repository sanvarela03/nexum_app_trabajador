package com.example.nexum_trabajador.ui.presenter.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.example.nexum_trabajador.domain.model.ConnectionState
import com.example.nexum_trabajador.domain.model.Message
import com.example.nexum_trabajador.domain.model.MessageStatus
import com.example.nexum_trabajador.domain.model.Profile
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 4.5
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    conversationId: String,
    receiverId: String,
    receiverRole: String,
    currentUserId: String,
    onNavigateBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val profiles by viewModel.profiles.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState(initial = ConnectionState.DISCONNECTED)
    val isReceiverTyping by viewModel.isReceiverTyping.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val receiverProfile = remember(profiles, receiverId) {
        profiles.find { it.id.toString() == receiverId }
    }

    LaunchedEffect(receiverId) {
        viewModel.fetchReceiverProfile(receiverId)
    }

    DisposableEffect(conversationId, receiverId, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    viewModel.connectWebSocket()
                    viewModel.loadConversationMessages(conversationId, receiverId)
                }
                Lifecycle.Event.ON_STOP -> {
                    viewModel.disconnectWebSocket()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.disconnectWebSocket()
        }
    }

    Nexum_trabajadorTheme {
        ChatScreenContent(
            messages = messages,
            profile = receiverProfile,
            uiState = uiState,
            connectionState = connectionState,
            currentUserId = currentUserId,
            receiverId = receiverId,
            isReceiverTyping = isReceiverTyping,
            onNavigateBack = onNavigateBack,
            onSendMessage = { content ->
                viewModel.sendMessage(receiverId, receiverRole, content)
            },
            onTyping = {
                viewModel.setTyping()
            },
            onRetry = {
                viewModel.clearError()
                viewModel.loadConversationMessages(conversationId, receiverId)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
private fun ChatScreenContent(
    messages: List<Message>,
    profile: Profile?,
    uiState: ChatUiState,
    connectionState: ConnectionState,
    currentUserId: String,
    receiverId: String,
    isReceiverTyping: Boolean,
    onNavigateBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onTyping: () -> Unit,
    onRetry: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val chatItems = remember(messages) {
        val items = mutableListOf<Any>()
        // Ensure messages are unique by ID even here as a failsafe
        val uniqueMessages = messages.distinctBy { it.id }
        
        for (i in uniqueMessages.indices) {
            val currentMessage = uniqueMessages[i]
            items.add(currentMessage)
            
            val currentInstant = try { Instant.parse(currentMessage.timestamp) } catch (e: Exception) { Instant.now() }
            val currentDate = currentInstant.atZone(ZoneId.systemDefault()).toLocalDate()
            
            val nextDate = if (i + 1 < uniqueMessages.size) {
                val nextInstant = try { Instant.parse(uniqueMessages[i + 1].timestamp) } catch (e: Exception) { null }
                nextInstant?.atZone(ZoneId.systemDefault())?.toLocalDate()
            } else null
            
            if (currentDate != nextDate) {
                items.add(currentDate)
            }
        }
        items
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(0)
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            Surface(
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    title = {
                        val fallbackUrl = remember(profile) {
                            val name = profile?.let { 
                                "${it.firstName.trim()} ${it.lastName.trim()}".replace("\\s+".toRegex(), "+")
                            } ?: "U"
                            "https://ui-avatars.com/api/?name=$name&background=random&format=png"
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val validImgUrl = profile?.imgUrl?.takeIf {
                                it.isNotBlank() && it != "NaN" && it != "null" && it.startsWith("http") && !it.contains("localhost") && !it.contains("127.0.0.1")
                            }

                            GlideImage(
                                model = validImgUrl ?: fallbackUrl,
                                contentDescription = "Perfil",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                requestBuilderTransform = {
                                    it.signature(ObjectKey(profile?.id ?: receiverId))
                                      .error(it.clone().load(fallbackUrl))
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = profile?.let { "${it.firstName} ${it.lastName}" } ?: "Chat con: $receiverId",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = profile?.email ?: "NaN",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Light
                                )
                                Text(
                                    text = when (connectionState) {
                                        ConnectionState.CONNECTED -> "Conectado"
                                        ConnectionState.CONNECTING -> "Conectando..."
                                        ConnectionState.RECONNECTING -> "Reconectando..."
                                        ConnectionState.DISCONNECTED -> "Desconectado"
                                        ConnectionState.ERROR -> "Error de conexión"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = when (connectionState) {
                                        ConnectionState.CONNECTED -> Color(0xFF4CAF50)
                                        ConnectionState.RECONNECTING, ConnectionState.CONNECTING -> Color(0xFFFF9800)
                                        ConnectionState.ERROR -> Color.Red
                                        else -> Color.Gray
                                    }
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    }
                )
            }
        },
        bottomBar = {
            Column {
                if (isReceiverTyping) {
                    Text(
                        text = "Escribiendo...",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                            .align(Alignment.Start)
                    )
                }
                ChatInputBar(
                    text = messageText,
                    onTextChange = { 
                        messageText = it
                        onTyping()
                    },
                    onSendClick = {
                        if (messageText.isNotBlank()) {
                            onSendMessage(messageText.trim())
                            messageText = ""
                        }
                    },
                    enabled = connectionState == ConnectionState.CONNECTED
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is ChatUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is ChatUiState.Error -> ErrorMessage(uiState.message, onRetry)
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        reverseLayout = true,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
                    ) {
                        items(items = chatItems, key = { item ->
                            when (item) {
                                is Message -> "msg_${item.id}"
                                is LocalDate -> "header_${item}"
                                else -> "other_${item.hashCode()}"
                            }
                        }) { item ->
                            when (item) {
                                is Message -> MessageItem(item, item.senderId == currentUserId)
                                is LocalDate -> DateHeader(item)
                            }
                        }
                        if (uiState is ChatUiState.LoadingMore) {
                            item {
                                Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    enabled: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje...") },
                shape = RoundedCornerShape(24.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = onSendClick,
                enabled = enabled && text.isNotBlank(),
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (enabled && text.isNotBlank()) MaterialTheme.colorScheme.primary else Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = Color.White
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MessageItem(message: Message, isFromMe: Boolean) {
    val alignment = if (isFromMe) Alignment.End else Alignment.Start
    val bubbleColor = if (isFromMe) MaterialTheme.colorScheme.primary else Color(0xFFF1F0F0)
    val textColor = if (isFromMe) Color.White else Color.Black

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isFromMe) 16.dp else 4.dp,
                bottomEnd = if (isFromMe) 4.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.content,
                color = textColor,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
        ) {
            Text(
                text = formatMessageTime(message.timestamp),
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Light
            )
            
            if (isFromMe) {
                Spacer(Modifier.width(4.dp))
                val icon = when (message.status) {
                    MessageStatus.SENT -> Icons.Default.Check
                    MessageStatus.DELIVERED -> Icons.Default.DoneAll
                    MessageStatus.READ -> Icons.Default.DoneAll
                }
                val iconTint = if (message.status == MessageStatus.READ) Color(0xFF2196F3) else Color.Gray // Blue for read, Gray for sent/delivered
                
                Icon(
                    imageVector = icon,
                    contentDescription = message.status.name,
                    tint = iconTint,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatMessageTime(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
        dateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateHeader(date: LocalDate) {
    val dateText = remember(date) {
        val now = LocalDate.now()
        when (date) {
            now -> "Hoy"
            now.minusDays(1) -> "Ayer"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM", Locale("es", "ES"))
                date.format(formatter)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.LightGray.copy(alpha = 0.3f),
            shape = CircleShape
        ) {
            Text(
                text = dateText,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
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
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
