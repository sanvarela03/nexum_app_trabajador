package com.example.nexum_trabajador.ui.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.nexum_trabajador.ui.theme.componentShapes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@Composable
fun MyTextFieldComponent2(
    labelValue: String,
    textValue: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = true,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(componentShapes.small),
) {
    Log.d("MyTextFieldComponent2", "leadingIcon: $leadingIcon")
    Log.d("MyTextFieldComponent2", "leadingIcon is not null: ${leadingIcon != null}")

    Log.d("MyTextFieldComponent2", "trailingIcon: $trailingIcon")
    Log.d("MyTextFieldComponent2", "trailingIcon is not null: ${trailingIcon != null}")

    OutlinedTextField(
        value = textValue,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,        // cuando está enfocado
            unfocusedBorderColor = Color(0xFFE6E6E6), // cuando no está enfocado
            errorBorderColor = Color.Red,
            unfocusedLabelColor = Color(0xFFE6E6E6),
            unfocusedLeadingIconColor = Color(0xFFE6E6E6),
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        label = {
            Text(
                text = labelValue,
                fontWeight = FontWeight.Medium
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        onValueChange = {
            onTextSelected(it)
        },
        leadingIcon = {
            if (leadingIcon != null) {
                leadingIcon()
            }
        },
        trailingIcon = {
            if (trailingIcon != null) {
                trailingIcon()
            }
        },
        isError = !errorStatus
    )
}
