package com.example.nexum_trabajador.domain.use_cases.notification

import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import com.example.nexum_trabajador.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotifications @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<NotificationEntity>> {
        return repository.getNotifications()
    }
}