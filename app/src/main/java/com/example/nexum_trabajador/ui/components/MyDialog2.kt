package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
fun MyDialog2(
    title: String,
    show: Boolean,
    dismissTxt: String = "Rechazar",
    confirmTxt: String = "Confirmar",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (show) {
        AlertDialog(
            containerColor = Color.White,
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                content()
            },
            onDismissRequest = { onDismiss() },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(
                        text = dismissTxt,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(
                        text = confirmTxt,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                }
            },
            modifier = modifier.width(500.dp),
            properties = DialogProperties(
                usePlatformDefaultWidth = false,

                )
        )
    }
}
