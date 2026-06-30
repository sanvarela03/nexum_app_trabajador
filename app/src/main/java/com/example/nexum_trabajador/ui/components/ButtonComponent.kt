package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ButtonComponent(
    value: String,
    isEnabled: Boolean = false,
    isLoading: Boolean = false,
    onButtonClicked: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = { onButtonClicked() },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            disabledContainerColor = Color.Gray,
            contentColor = Color.White,
            disabledContentColor = Color.Gray
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = isEnabled
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

}

@Composable
fun ButtonComponent(
    value: String,
    isEnabled: Boolean = false,
    isLoading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onButtonClicked: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = { onButtonClicked() },
        contentPadding = PaddingValues(),
        colors = colors,
        shape = RoundedCornerShape(8.dp),
        enabled = isEnabled
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

}