package com.example.nexum_trabajador.domain.use_cases.notification

import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import com.example.nexum_trabajador.domain.repository.NotificationRepository
import javax.inject.Inject

class SaveNotification @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationEntity: NotificationEntity) {
        repository.saveNotification(notificationEntity)
    }
}