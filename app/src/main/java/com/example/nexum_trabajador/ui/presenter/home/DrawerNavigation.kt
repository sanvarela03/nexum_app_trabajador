package com.example.nexum_trabajador.ui.presenter.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.nexum_trabajador.ui.navigation.rutes.DrawerRoutes


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
enum class DrawerNavigation(
    val title: String,
    val description: String,
    val route: DrawerRoutes,
    val icon: ImageVector,
    val selectedIcon: ImageVector?,
    val unselectedIcon: ImageVector?,
) {
    PROFILE(
        title = "Mi Perfil",
        description = "Home",
        route = DrawerRoutes.ProfileScreen,
        icon = Icons.Outlined.AccountCircle,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
    ),
    OCCUPATIONS(
        title = "Mis Profesiones",
        description = "Home",
        route = DrawerRoutes.OccupationsScreen,
        icon = Icons.Outlined.WorkOutline,
        selectedIcon = Icons.Filled.Work,
        unselectedIcon = Icons.Outlined.WorkOutline,
    ),
    INSURANCES(
        title = "Mis Seguros y Pólizas",
        description = "Home",
        route = DrawerRoutes.InsurancesScreen,
        icon = Icons.Outlined.Shield,
        selectedIcon = Icons.Filled.Shield,
        unselectedIcon = Icons.Outlined.Shield,
    ),
    ADDRESS(
        title = "Ubicación de trabajo",
        description = "Home",
        route = DrawerRoutes.AddressScreen,
        icon = Icons.Outlined.Map,
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
    ),
    SETTINGS(
        title = "Ajustes",
        description = "Home",
        route = DrawerRoutes.SettingsScreen,
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    ),
}