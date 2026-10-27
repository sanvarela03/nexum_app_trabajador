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
        val isJobOffer = (title?.contains("Nueva oferta de trabajo", ignoreCase = true) == true) ||
                (message.data["type"] == "NEW_JOB_OFFER") ||
                (message.data["title"]?.contains("Nueva oferta de trabajo", ignoreCase = true) == true) ||
                (message.notification?.title?.contains("Nueva oferta de trabajo", ignoreCase = true) == true)

        val channelId = if (isJobOffer) CHANNEL_HIGH_IMPORTANCE_ID else CHANNEL_DEFAULT_ID
        val channelName = if (isJobOffer) CHANNEL_HIGH_IMPORTANCE_NAME else CHANNEL_DEFAULT_NAME
        val importance = if (isJobOffer) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                importance
            ).apply {
                if (isJobOffer) {
                    description = "Notificaciones de alta prioridad para nuevas ofertas de trabajo cercanas"
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 300, 200, 300)
                    enableLights(true)
                    lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                } else {
                    description = "Notificaciones generales de Nexum"
                }
            }
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
            .setContentTitle(title ?: if (isJobOffer) "Nueva oferta de trabajo cerca de ti" else "No tiene titulo")
            .setContentText(body ?: "No tiene cuerpo")
            .setStyle(NotificationCompat.BigTextStyle().bigText(body ?: "No tiene cuerpo"))
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (isJobOffer) {
            notificationBuilder
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVibrate(longArrayOf(0, 300, 200, 300))
        } else {
            notificationBuilder
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        // Mostrar la notificación
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
        const val CHANNEL_HIGH_IMPORTANCE_ID = "job_offers_high_importance_channel"
        const val CHANNEL_HIGH_IMPORTANCE_NAME = "Ofertas de Trabajo"
        const val CHANNEL_DEFAULT_ID = "Default"
        const val CHANNEL_DEFAULT_NAME = "General"
    }
}
