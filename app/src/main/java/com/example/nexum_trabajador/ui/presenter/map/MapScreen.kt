package com.example.nexum_trabajador.ui.presenter.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import timber.log.Timber

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
    onMapClick: (LatLng, String) -> Unit,
    latitude: Double? = null,
    longitude: Double? = null
) {
    val context = LocalContext.current

    val initialPosition: LatLng? = remember(latitude, longitude) {
        if (latitude != null && longitude != null) {
            LatLng(latitude, longitude)
        } else {
            null
        }
    }

    val defaultLocation = LatLng(51.5074, -0.1278) // London

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition ?: defaultLocation, 15f)
    }
    val markerState = rememberMarkerState(position = initialPosition ?: defaultLocation)

    val userLocation = viewModel.userLocation.value
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var locationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
        if (isGranted) {
            viewModel.fetchUserLocation(context, fusedLocationClient)
        } else {
            Timber.e("Location permission was denied by the user.")
        }
    }

    LaunchedEffect(Unit) {
        if (initialPosition == null && !locationPermissionGranted) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else if (locationPermissionGranted) {
            viewModel.fetchUserLocation(context, fusedLocationClient)
        }
    }

    LaunchedEffect(userLocation) {
        if (initialPosition == null) { // Only if we are fetching user location
            userLocation?.let {
                markerState.position = it
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                viewModel.getAddressFromLatLng(context, it.latitude, it.longitude) { address ->
                    onMapClick(it, address ?: "")
                }
            }
        }
    }

    LaunchedEffect(initialPosition) {
        initialPosition?.let { position ->
            // Move camera and marker to the initial position
            markerState.position = position
            cameraPositionState.position = CameraPosition.fromLatLngZoom(position, 15f)
            viewModel.getAddressFromLatLng(context, position.latitude, position.longitude) { address ->
                onMapClick(position, address ?: "")
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = locationPermissionGranted
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = locationPermissionGranted
        ),
        onMapClick = { latLng ->
            markerState.position = latLng
            viewModel.getAddressFromLatLng(
                context = context,
                lat = latLng.latitude,
                lng = latLng.longitude
            ) { address ->
                onMapClick(latLng, address ?: "")
            }
        }
    ) {
        Marker(
            state = markerState,
            title = "Ubicación de trabajo",
            snippet = "Ubicación seleccionada"
        )
    }
}
