package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 21/08/2025
 * @version 1.0
 */
@Composable
fun UploadImageButtonComponent(
    text: String = "",
    leadingIcon: Painter,
    leadingIconTint: Color = Color.Unspecified,
    trailingIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
    onClick: () -> Unit = {},
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = Color(0xFFD9D9D9)),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = leadingIcon,
                contentDescription = "Pick photo",
                modifier = Modifier.size(20.dp),
                tint = leadingIconTint
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    color = Color(0xFF000000)
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = trailingIcon,
                contentDescription = "Pick photo",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}