package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nexum_trabajador.ui.navigation.rutes.DrawerRoutes
import com.example.nexum_trabajador.ui.presenter.home.DrawerNavigation

@Composable
fun NavigationDrawerBody(
    items: List<DrawerNavigation>,
    navigateTo: (DrawerRoutes) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items) { navItem: DrawerNavigation ->
            NavigationItemRow(item = navItem, navigateTo = { navigateTo(navItem.route) })
        }
    }
}
