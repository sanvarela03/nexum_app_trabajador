package com.example.nexum_trabajador.domain.use_cases.notification

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class NotificationUseCases @Inject constructor(
    val saveNotification: SaveNotification,
    val getNotifications: GetNotifications,
    val deleteNotification: DeleteNotification,
    val getNotificationById: GetNotificationById
)
