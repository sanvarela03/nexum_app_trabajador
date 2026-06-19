package com.example.nexum_trabajador.ui.presenter.home


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/1/2025
 * @version 1.0
 */
sealed class HomeEvent {
    object SignOutBtnClicked : HomeEvent()
    data class LatitudeChanged(val latitude: Double) : HomeEvent()
    data class LongitudeChanged(val longitude: Double) : HomeEvent()
    data class AddressChanged(val address: String) : HomeEvent()

    object Refresh : HomeEvent()
    object UpdateWorkerDetails : HomeEvent()
    object DismissDialog : HomeEvent()
}
