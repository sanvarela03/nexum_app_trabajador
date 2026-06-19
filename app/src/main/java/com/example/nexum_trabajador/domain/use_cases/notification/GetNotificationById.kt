package com.example.nexum_trabajador.domain.use_cases.notification

import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import com.example.nexum_trabajador.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationById @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Int): NotificationEntity? {
        return repository.getNotificationById(id)
    }
}
