package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.ui.navigation.rutes.DrawerRoutes
import com.example.nexum_trabajador.ui.presenter.home.DrawerNavigation

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/1/2025
 * @version 1.0
 */
@Composable
fun NavigationItemRow(
    item: DrawerNavigation,
    navigateTo: (DrawerRoutes) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateTo(item.route) }
            .padding(all = 20.dp),
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.description
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = item.title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
            )
        )
    }
}