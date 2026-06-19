package com.example.nexum_trabajador.data.notification

import com.example.nexum_trabajador.data.notification.local.NotificationDao
import com.example.nexum_trabajador.data.notification.local.NotificationEntity
import com.example.nexum_trabajador.data.notification.local.NotificationLocalDataSource
import com.example.nexum_trabajador.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val localDataSource: NotificationLocalDataSource
) : NotificationRepository {
    override suspend fun saveNotification(notificationEntity: NotificationEntity) {
        localDataSource.insert(notificationEntity)
    }

    override suspend fun deleteNotification(notificationId: Int) {
        localDataSource.delete(notificationId)
    }

    override fun getNotifications(): Flow<List<NotificationEntity>> {
        return localDataSource.getAll()
    }

    override suspend fun getNotificationById(id: Int): NotificationEntity? {
        return localDataSource.getOne(id)
    }
}