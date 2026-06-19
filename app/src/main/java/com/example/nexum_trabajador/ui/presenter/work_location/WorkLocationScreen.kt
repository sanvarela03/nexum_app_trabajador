package com.example.nexum_trabajador.ui.presenter.work_location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nexum_trabajador.domain.model.Worker
import com.example.nexum_trabajador.ui.components.ButtonComponent
import com.example.nexum_trabajador.ui.components.DialogComponent
import com.example.nexum_trabajador.ui.presenter.home.HomeEvent
import com.example.nexum_trabajador.ui.presenter.home.HomeViewModel
import com.example.nexum_trabajador.ui.presenter.map.MapScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkLocationScreen(
    homeViewModel: HomeViewModel,
    onBack: () -> Unit
) {
    val state = homeViewModel.state
    val worker: Worker? = state.worker

    LaunchedEffect(Unit) {
        homeViewModel.refreshWorker(fetchFromRemote = true)
    }

    if (state.showDialog) {
        DialogComponent(
            onDismissRequest = { homeViewModel.onEvent(HomeEvent.DismissDialog) },
            onConfirmation = {
                if (state.isSuccess) {
                    homeViewModel.onEvent(HomeEvent.DismissDialog)
                    onBack()
                } else {
                    homeViewModel.onEvent(HomeEvent.DismissDialog)
                }
            },
            dialogTitle = state.dialogTitle,
            dialogText = state.dialogMessage
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(bottom = 5.dp, top = 5.dp),
                windowInsets = WindowInsets(
                    left = 0.dp,
                    top = 0.dp,
                    right = 0.dp,
                ),
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MapScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                latitude = worker?.latitude,
                longitude = worker?.longitude,
                onMapClick = { latLng, address ->
                    homeViewModel.onEvent(
                        HomeEvent.LatitudeChanged(latLng.latitude)
                    )
                    homeViewModel.onEvent(
                        HomeEvent.LongitudeChanged(latLng.longitude)
                    )
                    homeViewModel.onEvent(
                        HomeEvent.AddressChanged(address)
                    )
                }
            )
            ButtonComponent(
                value = "Actualizar ubicación",
                isEnabled = worker?.latitude != null && worker.longitude != null,
                isLoading = state.isUpdating,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White,
                    disabledContentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                homeViewModel.onEvent(HomeEvent.UpdateWorkerDetails)
            }
        }
    }
}
