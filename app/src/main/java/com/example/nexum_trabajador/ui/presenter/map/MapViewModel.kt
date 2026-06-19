package com.example.nexum_trabajador.ui.presenter.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    // State to hold the user's location as LatLng (latitude and longitude)
    private val _userLocation = mutableStateOf<LatLng?>(null)
    val userLocation: State<LatLng?> = _userLocation

    // Function to fetch the user's location and update the state
    fun fetchUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {
        // Check if the location permission is granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                // Fetch the last known location
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        // Update the user's location in the state
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        _userLocation.value = userLatLng
                    }
                }
            } catch (e: SecurityException) {
                Timber.e("Permission for location access was revoked: ${e.localizedMessage}")
            }
        } else {
            Timber.e("Location permission is not granted.")
        }
    }

    fun getAddressFromLatLng(
        context: Context,
        lat: Double,
        lng: Double,
        onResult: (String?) -> Unit
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // API 33+ (Android 13) usa callback asíncrono
            geocoder.getFromLocation(lat, lng, 1) { addresses: List<Address> ->
                if (addresses.isNotEmpty()) {
                    onResult(addresses[0].getAddressLine(0))
                } else {
                    onResult(null)
                }
            }
        } else {
            // Versiones antiguas: llamada síncrona
            try {
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (!addresses.isNullOrEmpty()) {
                    onResult(addresses[0].getAddressLine(0))
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }
}