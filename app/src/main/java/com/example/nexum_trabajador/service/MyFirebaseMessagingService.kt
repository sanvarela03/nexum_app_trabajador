package com.example.nexum_trabajador.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.nexum_trabajador.MainActivity
import com.example.nexum_trabajador.R
import com.example.nexum_trabajador.domain.use_cases.notification.NotificationUseCases
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@AndroidEntryPoint
@Singleton
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationsUseCases: NotificationUseCases

    @Inject
    lateinit var updateFcmTokenUseCase: com.example.nexum_trabajador.domain.use_cases.user.UpdateFcmTokenUseCase

    @Inject
    lateinit var tokenManager: com.example.nexum_trabajador.security.TokenManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.v(TAG, "MyFirebaseMessagingService")
        Log.v(TAG, message.notification?.title ?: "No tiene titulo")
        Log.v(TAG, message.notification?.body ?: "No tiene cuerpo")

        message.data.forEach { (key, value) ->
            Log.v(TAG, "| Llave : $key | Valor: $value |")
        }

        val notification = NotificationDataMapper.toNotificationEntity(message)

        if (notification != null) {
            CoroutineScope(Dispatchers.IO).launch {
                notificationsUseCases.saveNotification(notification)
            }
        }

        showNotification(message, notification?.title, notification?.body)
    }


    override fun onNewToken(token: String) {
        Log.v(TAG, "Refreshed firebase_token: $token")
        super.onNewToken(token)
        if (tokenManager.getAccessTokenSync() != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    updateFcmTokenUseCase(token).collect { response ->
                        Log.d(TAG, "FCM token synced to backend: $response")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error syncing refreshed FCM token", e)
                }
            }
        }
    }

    private fun showNotification(message: RemoteMessage, title: String?, body: String?) {
        val channelId = "Default"
        val channelName = "Default Channel"

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = message.data["jobOfferId"]?.toIntOrNull()
            ?: message.data["contractId"]?.toIntOrNull()
            ?: (System.currentTimeMillis() % 100000).toInt()

        // Intentar abrir MainActivity cuando se haga clic en la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            // Extraer payload si viene del backend
            message.data["type"]?.let { putExtra("type", it) }
            message.data["jobOfferUuid"]?.let { putExtra("jobOfferUuid", it) }
            message.data["jobOfferId"]?.let { putExtra("jobOfferId", it) }
            message.data["contractId"]?.let { putExtra("contractId", it) }
        }

        // Crear PendingIntent para la actividad principal
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intentar realizar alguna acción cuando se hagan clic en los botones
        val acceptIntent = Intent(this, AcceptBroadcastReceiver::class.java)
        val acceptPendingIntent = PendingIntent.getBroadcast(
            this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectIntent = Intent(this, RejectBroadcastReceiver::class.java)
        val rejectPendingIntent = PendingIntent.getBroadcast(
            this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Construir la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.nexum_logo_2)
            .setContentTitle(title ?: "No tiene titulo")
            .setSound(defaultSoundUri)
            .setContentText(body ?: "No tiene cuerpo")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Mostrar la notificación
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}
