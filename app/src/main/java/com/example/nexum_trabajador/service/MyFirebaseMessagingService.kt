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
import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import com.example.nexum_trabajador.domain.use_cases.notification.NotificationUseCases
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


@AndroidEntryPoint
@Singleton
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationsUseCases: NotificationUseCases

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.v(TAG, "MyFirebaseMessagingService")
        Log.v(TAG, message.notification?.title ?: "No tiene titulo")
        Log.v(TAG, message.notification?.body ?: "No tiene cuerpo")

        message.data.forEach { (key, value) ->
            Log.v(TAG, "| Llave : $key | Valor: $value |")
        }

        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]
        
        val type = message.data["type"]
        val jobOfferUuid = message.data["jobOfferUuid"]
        val clientName = message.data["clientName"]
        val clientImageUrl = message.data["clientImageUrl"]
        val jobOfferTitle = message.data["jobOfferTitle"]
        val requiredDate = message.data["requiredDate"]
        val locationName = message.data["locationName"]
        val categoryName = message.data["categoryName"]
        val serviceType = message.data["serviceType"]

        if (title != null && body != null) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")
            val formatted = current.format(formatter)
            val notification = NotificationEntity(
                title = title,
                body = body,
                createdAt = formatted,
                type = type,
                jobOfferUuid = jobOfferUuid,
                clientName = clientName,
                clientImageUrl = clientImageUrl,
                jobOfferTitle = jobOfferTitle,
                requiredDate = requiredDate,
                locationName = locationName,
                categoryName = categoryName,
                serviceType = serviceType
            )
            CoroutineScope(Dispatchers.IO).launch {
                notificationsUseCases.saveNotification(notification)
            }
        }
        showNotification(message, title, body)
    }


    override fun onNewToken(token: String) {
        Log.v(TAG, "Refreshed firebase_token: $token")
        super.onNewToken(token)
//        CoroutineScope(Dispatchers.IO).launch {
//            producerUseCases.updateFirebaseToken(token)
//        }
    }

    private fun showNotification(message: RemoteMessage, title: String?, body: String?) {
        val channelId = "Default"
        val channelName = "Default Channel"

        // Crear un NotificationManager
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Crear un canal de notificación (necesario para Android Oreo y versiones posteriores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intentar abrir tu actividad cuando se haga clic en la notificación
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Extraer payload si viene del backend
        message.data["type"]?.let { intent.putExtra("type", it) }
        message.data["jobOfferUuid"]?.let { intent.putExtra("jobOfferUuid", it) }
        message.data["jobOfferId"]?.let { intent.putExtra("jobOfferId", it) }

        // Crear PendingIntent para la actividad principal
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
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

        // Construir la notificación con botones
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.nexum_logo_2)
            .setContentTitle(title ?: "No tiene titulo")
            .setSound(defaultSoundUri)
            .setContentText(body ?: "No tiene cuerpo")
            .setContentIntent(pendingIntent)


        // Mostrar la notificación
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}