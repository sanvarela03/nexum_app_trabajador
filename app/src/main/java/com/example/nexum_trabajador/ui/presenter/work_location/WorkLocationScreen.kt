package com.example.nexum_trabajador.ui.presenter.work_location

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import com.example.nexum_trabajador.domain.model.Worker
import com.example.nexum_trabajador.ui.components.DialogComponent
import com.example.nexum_trabajador.ui.presenter.home.HomeEvent
import com.example.nexum_trabajador.ui.presenter.home.HomeViewModel
import com.example.nexum_trabajador.ui.presenter.map.MapScreen
import com.google.android.gms.maps.model.LatLng

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 2.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkLocationScreen(
    homeViewModel: HomeViewModel,
    onBack: (() -> Unit)? = null,
    onNavigateToOccupations: () -> Unit = {}
) {
    val state = homeViewModel.state
    val worker: Worker? = state.worker
    val isAvailable = worker?.isAvailable ?: true

    LaunchedEffect(Unit) {
        homeViewModel.refreshWorker(fetchFromRemote = true)
        homeViewModel.loadOccupations()
    }

    if (state.showDialog) {
        DialogComponent(
            onDismissRequest = { homeViewModel.onEvent(HomeEvent.DismissDialog) },
            onConfirmation = {
                homeViewModel.onEvent(HomeEvent.DismissDialog)
                if (state.isSuccess) {
                    onBack?.invoke()
                }
            },
            dialogTitle = state.dialogTitle,
            dialogText = state.dialogMessage
        )
    }

    val content: @Composable (Modifier) -> Unit = { modifier ->
        WorkLocationContent(
            modifier = modifier,
            worker = worker,
            isAvailable = isAvailable,
            isTogglingAvailability = state.isTogglingAvailability,
            isUpdating = state.isUpdating,
            occupations = state.occupations,
            onToggleAvailability = { nextState ->
                homeViewModel.onEvent(HomeEvent.ToggleAvailability(nextState))
            },
            onSaveLocation = {
                homeViewModel.onEvent(HomeEvent.UpdateWorkerDetails)
            },
            onMapClick = { latLng, address ->
                homeViewModel.onEvent(HomeEvent.LatitudeChanged(latLng.latitude))
                homeViewModel.onEvent(HomeEvent.LongitudeChanged(latLng.longitude))
                homeViewModel.onEvent(HomeEvent.AddressChanged(address))
            },
            onNavigateToOccupations = onNavigateToOccupations
        )
    }

    if (onBack != null) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(bottom = 5.dp, top = 5.dp),
                    windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    title = {
                        Text(
                            text = "Ubicación de trabajo",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            content(Modifier.padding(paddingValues))
        }
    } else {
        content(Modifier.fillMaxSize())
    }
}

@Composable
private fun WorkLocationContent(
    modifier: Modifier = Modifier,
    worker: Worker?,
    isAvailable: Boolean,
    isTogglingAvailability: Boolean,
    isUpdating: Boolean,
    occupations: List<JobOccupationRes>,
    onToggleAvailability: (Boolean) -> Unit,
    onSaveLocation: () -> Unit,
    onMapClick: (LatLng, String) -> Unit,
    onNavigateToOccupations: () -> Unit
) {
    val activeOccupations = occupations.filter { it.isActive ?: true }
    val activeCategories = activeOccupations.mapNotNull { it.categoryName }.distinct()
    val hasNoActiveOccupations = activeOccupations.isEmpty()

    // Pulse animation for Uber Online status dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Full screen Google Map
        MapScreen(
            modifier = Modifier.fillMaxSize(),
            latitude = worker?.latitude,
            longitude = worker?.longitude,
            onMapClick = onMapClick
        )

        // Top Area: Status Pill and Alert or Active Categories indicator
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Uber Floating Top Status Pill
            Surface(
                shape = RoundedCornerShape(50),
                color = if (isAvailable) Color(0xFF1B5E20).copy(alpha = 0.92f) else Color(0xFF263238).copy(alpha = 0.92f),
                shadowElevation = 8.dp,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isAvailable) Color(0xFF4CAF50).copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isAvailable) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00E676).copy(alpha = pulseAlpha))
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00E676))
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFB0BEC5))
                            )
                        }
                    }
                    Text(
                        text = if (isAvailable) "EN LÍNEA" else "DESCONECTADO",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Alerta si NO tiene profesiones seleccionadas / activas
            if (hasNoActiveOccupations) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    border = BorderStroke(1.dp, Color(0xFFFFB74D)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToOccupations() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF9800).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsOff,
                                contentDescription = null,
                                tint = Color(0xFFE65100),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (occupations.isEmpty()) "Sin profesiones configuradas" else "Profesiones en pausa",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFFE65100)
                            )
                            Text(
                                text = "No recibirás notificaciones ni solicitudes hasta que selecciones una o más profesiones.",
                                fontSize = 11.sp,
                                color = Color(0xFF5D4037),
                                lineHeight = 15.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else {
                // Mini indicador compacto flotante de categorías seleccionadas
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    modifier = Modifier.clickable { onNavigateToOccupations() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = null,
                            tint = Color(0xFF4B7B2B),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Categorías activas (${activeCategories.size}):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF424242)
                        )
                        Text(
                            text = activeCategories.joinToString(", "),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4B7B2B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 160.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        var isMinimized by rememberSaveable { mutableStateOf(false) }

        // Uber Floating Bottom Control Card (Solid Opaque White with Minimize/Maximize)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .animateContentSize(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Status Header, Toggle Switch and Minimize/Maximize button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isAvailable) Color(0xFF00C853) else Color(0xFF757575))
                        )
                        Column {
                            Text(
                                text = if (isAvailable) "Estás en línea" else "Estás fuera de línea",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isAvailable) Color(0xFF1B5E20) else Color(0xFF263238),
                                fontSize = 15.sp
                            )
                            if (!isMinimized) {
                                Text(
                                    text = if (isAvailable) "Recibiendo ofertas cercanas"
                                           else "Conéctate para recibir solicitudes",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF616161),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isTogglingAvailability) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFF4B7B2B)
                            )
                        } else {
                            Switch(
                                checked = isAvailable,
                                onCheckedChange = { onToggleAvailability(it) },
                                modifier = Modifier.scale(0.85f),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF00C853),
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color(0xFFB0BEC5)
                                )
                            )
                        }

                        // Botón para minimizar / maximizar la tarjeta
                        IconButton(
                            onClick = { isMinimized = !isMinimized },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isMinimized) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isMinimized) "Maximizar" else "Minimizar",
                                tint = Color(0xFF616161)
                            )
                        }
                    }
                }

                // Contenido expandible
                AnimatedVisibility(
                    visible = !isMinimized,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        HorizontalDivider(
                            color = Color(0xFFE0E0E0),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )

                        // Location Details Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Ubicación",
                                tint = Color(0xFF4B7B2B),
                                modifier = Modifier.size(22.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "UBICACIÓN DE COBERTURA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4B7B2B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = worker?.address ?: "Toca el mapa para fijar tu ubicación",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF212121),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        HorizontalDivider(
                            color = Color(0xFFE0E0E0),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )

                        // Categorías de Servicio Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToOccupations() },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Work,
                                    contentDescription = "Categorías",
                                    tint = if (activeCategories.isNotEmpty()) Color(0xFF4B7B2B) else Color(0xFFE65100),
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        text = "CATEGORÍAS DE SERVICIO",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (activeCategories.isNotEmpty()) Color(0xFF4B7B2B) else Color(0xFFE65100),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    if (activeCategories.isEmpty()) {
                                        Text(
                                            text = "⚠️ Sin profesiones activas. Toca para configurar.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFFE65100),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    } else {
                                        Text(
                                            text = activeCategories.joinToString(", "),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF212121),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                            TextButton(
                                onClick = onNavigateToOccupations,
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (activeCategories.isEmpty()) "Configurar" else "Editar",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Update Location Button
                            OutlinedButton(
                                onClick = onSaveLocation,
                                enabled = worker?.latitude != null && worker.longitude != null && !isUpdating,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (isUpdating) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Color(0xFF4B7B2B)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Guardar zona", fontSize = 12.sp, color = Color(0xFF212121))
                                }
                            }

                            // Main Action Uber Style Button
                            Button(
                                onClick = { onToggleAvailability(!isAvailable) },
                                enabled = !isTogglingAvailability,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isAvailable) Color(0xFFD32F2F) else Color(0xFF00C853),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = if (isAvailable) Icons.Default.PowerSettingsNew else Icons.Default.NearMe,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isAvailable) "Desconectar" else "Conectarse",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
