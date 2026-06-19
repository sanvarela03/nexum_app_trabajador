package com.example.nexum_trabajador.ui.presenter.job_offer_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.nexum_trabajador.R
import com.example.nexum_trabajador.data.job_offer.remote.payload.res.JobOfferWorkerDetailsDto
import com.example.nexum_trabajador.ui.presenter.map.MapScreen
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobOfferDetailsScreen(
    uuid: String,
    viewModel: JobOfferDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToApplication: (Long, String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(uuid) {
        viewModel.onEvent(JobOfferDetailsEvent.FetchDetails(uuid))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalles de la oferta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (state.errorMessage.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage, color = Color.Red)
            }
        } else {
            state.details?.let { details ->
                JobOfferDetailsContent(
                    details = details,
                    modifier = Modifier.padding(paddingValues),
                    onApply = { onNavigateToApplication(details.id, details.uuid) },
                    onDiscard = {
                        viewModel.discardDraft(details.id)
                        onNavigateBack()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobOfferDetailsContentPreview() {
    JobOfferDetailsContent(
        details = JobOfferWorkerDetailsDto(
            id = 1,
            uuid = "",
            title = "",
            description = "",
            categoryName = "",
            serviceType = "",
            iconName = "",
            photosUrl = listOf(""),
            latitude = 0.0,
            longitude = 0.0,
            address = "",
            requiredDate = "",
            clientName = "",
            clientImageUrl = "",
            isClientRecurring = false,
            distanceInMeters = 0.0,
            estimatedDrivingTimeMinutes = 0,
        ),
        onApply = {},
        onDiscard = {}
    )
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun JobOfferDetailsContent(
    details: JobOfferWorkerDetailsDto,
    modifier: Modifier = Modifier,
    onApply: () -> Unit,
    onDiscard: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Mapa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
        ) {
            MapScreen(
                modifier = Modifier.fillMaxSize(),
                latitude = details.latitude,
                longitude = details.longitude,
                onMapClick = { _, _ -> }
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título y tiempo estimado
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
                        getCategoryIcon(details.categoryName),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = details.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${details.estimatedDrivingTimeMinutes} min",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            // Chips de categoría y servicio
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(details.categoryName, fontSize = 12.sp) }
                )
                SuggestionChip(
                    onClick = {},
                    label = { Text(details.serviceType, fontSize = 12.sp) }
                )
            }

            // Descripción
            Text(text = details.description, fontSize = 14.sp, color = Color.Gray)

            // Evidencias (Fotos)
            if (details.photosUrl.isNotEmpty()) {
                Text(
                    text = "Evidencia adjunta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(details.photosUrl) { url ->
                        GlideImage(
                            model = url,
                            contentDescription = "Evidencia",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Dirección
            Text(
                text = "Dirección de la tarea",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.Gray)
                Text(text = details.address, fontSize = 14.sp, color = Color.Gray)
            }

            // Fecha requerida
            Text(
                text = "Fecha solicitada",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Color.Gray)
                Text(
                    text = formatRequiredDate(details.requiredDate),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(8.dp))

            // Cliente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlideImage(
                        model = details.clientImageUrl ?: R.drawable.nexum_logo_2,
                        contentDescription = "Cliente",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        failure = placeholder(R.drawable.nexum_logo_2)
                    )
                    Column {
                        Text(
                            text = details.clientName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (details.isClientRecurring) {
                            Text(
                                text = "Cliente Recurrente",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                Text(
                    text = "A ${String.format("%.1f", details.distanceInMeters / 1000)} km",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onDiscard,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Descartar", fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Aplicar", fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun formatRequiredDate(isoString: String): String {
    return try {
        val odt = OffsetDateTime.parse(isoString)
        val dateFormat = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es"))
        val timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        odt.format(dateFormat) + " • " + odt.format(timeFormat)
    } catch (e: Exception) {
        isoString
    }
}

private fun getCategoryIcon(categoryName: String): ImageVector {
    val lower = categoryName.lowercase()
    return when {
        lower.contains("electricidad") -> Icons.Filled.FlashOn
        lower.contains("cerrajería") || lower.contains("cerrajeria") -> Icons.Filled.Lock
        lower.contains("puertas") || lower.contains("ventanas") -> Icons.Filled.Home
        else -> Icons.Filled.Build // Plomería u otros por defecto (llave inglesa)
    }
}
