package com.example.nexum_trabajador.ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.ui.theme.InterFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(
    toolbarTitle: String,
    signOutButtonClicked: () -> Unit,
    navButtonClicked: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        title = {
            Text(
                text = toolbarTitle,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFontFamily,
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = { navButtonClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = ""
                )
            }
        },
        actions = {
            IconButton(onClick = { signOutButtonClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = ""
                )
            }
        }
    )
}
