package com.example.nexum_trabajador.service

import android.util.Log
import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import com.google.firebase.messaging.RemoteMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Mapea un RemoteMessage de Firebase a un NotificationEntity de Room para la app del trabajador.
 *
 * Extrae título, cuerpo y todos los campos del data payload,
 * centralizando la lógica de parsing en un solo lugar.
 */
object NotificationDataMapper {

    private const val TAG = "NotificationDataMapper"

    /**
     * Convierte un [RemoteMessage] a un [NotificationEntity].
     *
     * @return NotificationEntity si el mensaje tiene título y cuerpo, null en caso contrario.
     */
    fun toNotificationEntity(message: RemoteMessage): NotificationEntity? {
        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]

        if (title == null || body == null) {
            Log.w(TAG, "Mensaje FCM recibido sin título o cuerpo, ignorando.")
            return null
        }

        val createdAt = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"))

        val data = message.data

        return NotificationEntity(
            title = title,
            body = body,
            createdAt = createdAt,
            type = data["type"],
            jobOfferUuid = data["jobOfferUuid"],
            clientName = data["clientName"],
            clientImageUrl = data["clientImageUrl"],
            jobOfferTitle = data["jobOfferTitle"],
            requiredDate = data["requiredDate"],
            locationName = data["locationName"],
            categoryName = data["categoryName"],
            serviceType = data["serviceType"]
        )
    }
}
