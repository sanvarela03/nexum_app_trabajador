package com.example.nexum_trabajador.ui.presenter.notifications

sealed class NotificationsEvent {
    data class DeleteBtnClick(val id: Int) : NotificationsEvent()
}