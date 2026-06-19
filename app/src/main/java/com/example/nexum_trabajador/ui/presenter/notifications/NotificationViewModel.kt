package com.example.nexum_trabajador.ui.presenter.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_trabajador.domain.use_cases.notification.NotificationUseCases
import com.example.nexum_trabajador.domain.use_cases.job_application.DeleteJobApplicationDraftByUuidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationsUseCases: NotificationUseCases,
    private val deleteJobApplicationDraftByUuidUseCase: DeleteJobApplicationDraftByUuidUseCase
) : ViewModel() {
    val notifications = notificationsUseCases.getNotifications()

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            is NotificationsEvent.DeleteBtnClick -> {
                deleteNotification(event.id)
            }
        }
    }

    private fun deleteNotification(id: Int) {
        viewModelScope.launch {
            val notification = notificationsUseCases.getNotificationById(id)
            notification?.jobOfferUuid?.let { uuid ->
                deleteJobApplicationDraftByUuidUseCase(uuid)
            }
            notificationsUseCases.deleteNotification(id)
        }
    }
}