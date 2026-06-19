package com.example.nexum_trabajador.domain.use_cases.notification

import com.example.nexum_trabajador.domain.repository.NotificationRepository
import javax.inject.Inject


class DeleteNotification @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: Int) {
        repository.deleteNotification(notificationId)
    }
}