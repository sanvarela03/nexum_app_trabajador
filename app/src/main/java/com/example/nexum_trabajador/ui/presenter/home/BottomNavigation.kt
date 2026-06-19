package com.example.nexum_trabajador.ui.presenter.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.nexum_trabajador.ui.navigation.rutes.HomeRoutes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
enum class BottomNavigation(
    val title: String,
    val description: String,
    val route: HomeRoutes,
    val icon: ImageVector,
    val selectedIcon: ImageVector?,
    val unselectedIcon: ImageVector?,
) {
    HOME(
        title = "Inicio",
        description = "Home",
        route = HomeRoutes.CategoriesScreen,
        icon = Icons.Default.Home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    APPOINTMENTS(
        title = "Citas",
        description = "Home",
        route = HomeRoutes.AppointmentsScreen,
        icon = Icons.Default.EditCalendar,
        selectedIcon = Icons.Filled.EditCalendar,
        unselectedIcon = Icons.Outlined.EditCalendar,
    ),
    WALLET(
        title = "Cartera",
        description = "Home",
        route = HomeRoutes.WalletScreen,
        icon = Icons.Default.AccountBalanceWallet,
        selectedIcon = Icons.Filled.AccountBalanceWallet,
        unselectedIcon = Icons.Outlined.AccountBalanceWallet
    ),
    NOTIFICATIONS(
        title = "Notificaciones",
        description = "Home",
        route = HomeRoutes.NotificationsScreen,
        icon = Icons.Default.Notifications,
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications
    ),
}