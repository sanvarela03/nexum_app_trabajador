package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun saveNotification(notificationEntity: NotificationEntity)
    suspend fun deleteNotification(notificationId: Int)
    fun getNotifications(): Flow<List<NotificationEntity>>
    suspend fun getNotificationById(id: Int): NotificationEntity?
}