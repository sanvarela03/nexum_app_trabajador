package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun SignUpSuccessDialog(
    show: Boolean,
    username: String,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (show) {
        AlertDialog(
            containerColor = Color.White,
            title = {
                Text(
                    text = "Registro exitoso",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "El nombre de usuario autogenerado es: $username",
                    textAlign = TextAlign.Center
                )
            },
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = onAccept) {
                    Text(
                        text = "Aceptar",
                        color = Color.Black
                    )
                }
            },
            dismissButton = null,
            modifier = modifier.width(500.dp),
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}
