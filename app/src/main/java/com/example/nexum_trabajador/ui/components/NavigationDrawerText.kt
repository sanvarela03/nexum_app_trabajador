package com.example.nexum_trabajador.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun NavigationDrawerText(
    tittle: String,
    textUnit: TextUnit,
    color: Color
) {
    val shadowOffset = Offset(4f, 6f)

    Text(
        text = tittle,
        textAlign = TextAlign.Center,
        style = TextStyle(
            color = Color.Black,
            fontSize = textUnit,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            shadow = Shadow(
                color = Color.Gray,
                offset = shadowOffset, 2f
            )
        )
    )
}
