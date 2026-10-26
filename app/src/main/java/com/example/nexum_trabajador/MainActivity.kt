package com.example.nexum_trabajador

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.nexum_trabajador.app.WorkerApp
import com.example.nexum_trabajador.ui.global_viewmodels.AuthViewModel
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private var currentIntent by mutableStateOf<Intent?>(null)

    @javax.inject.Inject
    lateinit var updateFcmTokenUseCase: com.example.nexum_trabajador.domain.use_cases.user.UpdateFcmTokenUseCase

    @javax.inject.Inject
    lateinit var tokenManager: com.example.nexum_trabajador.security.TokenManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentIntent = intent
        enableEdgeToEdge()
        askNotificationPermission()
        syncFcmTokenIfLoggedIn()
        setContent {
            val userAuthState = authViewModel.isAuthenticated.collectAsState().value
            val destination = authViewModel.startDestinationData.collectAsState().value
            Nexum_trabajadorTheme {
                WorkerApp(
                    userAuthState = userAuthState,
                    startDestination = destination,
                    initialIntent = currentIntent
                )
            }
        }
    }

    private fun syncFcmTokenIfLoggedIn() {
        if (tokenManager.getAccessTokenSync() != null) {
            com.google.firebase.messaging.FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    if (!token.isNullOrBlank()) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                updateFcmTokenUseCase(token).collect { }
                            } catch (e: Exception) {
                                android.util.Log.e("MainActivity", "Error syncing FCM token on startup", e)
                            }
                        }
                    }
                }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntent = intent
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // Si el permiso no ha sido concedido, lo solicitamos
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // [START ask_post_notifications]
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }
}
