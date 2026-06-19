package com.example.nexum_trabajador.data.notification.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 5/30/2026
 * @version 1.0
 */
@Singleton
class NotificationLocalDataSource @Inject constructor(
    private val notificationDao: NotificationDao
) {
    suspend fun insert(notificationEntity: NotificationEntity) {
        notificationDao.insert(notificationEntity)
    }

    suspend fun delete(notificationId: Int) {
        notificationDao.deleteById(notificationId)
    }

    fun getAll(): Flow<List<NotificationEntity>> {
        return notificationDao.getAll()
    }

    suspend fun getOne(id: Int): NotificationEntity? {
        return notificationDao.getOne(id)
    }
}