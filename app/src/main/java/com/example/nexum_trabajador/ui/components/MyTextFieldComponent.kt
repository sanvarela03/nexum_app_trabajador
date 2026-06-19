package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.nexum_trabajador.ui.theme.componentShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(componentShapes.small),
    labelValue: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTextSelected: (String) -> Unit,
    textValue: String = "",
    errorStatus: Boolean = false,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }


    val unfocusedBorderColor =
        if (errorStatus && isFocused) Color.Green else MaterialTheme.colorScheme.secondary
    OutlinedTextField(
        value = textValue,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,        // cuando está enfocado
            unfocusedBorderColor = Color(0xFFE6E6E6), // cuando no está enfocado
            errorBorderColor = Color.Red,
            unfocusedLabelColor = Color(0xFFE6E6E6),
            unfocusedLeadingIconColor = Color(0xFFE6E6E6)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.onFocusEvent {
            if (it.isFocused) {
                // Notificamos al padre que perdió el foco
                isFocused = true
            }
        },
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
        leadingIcon = leadingIcon?.let { nonNullIcon ->
            { Icon(nonNullIcon, contentDescription = null) }
        },
        trailingIcon = trailingIcon?.let { nonNullIcon ->
            { Icon(nonNullIcon, contentDescription = null) }
        },
        isError = if (isFocused) errorStatus else false
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(componentShapes.small),
    labelValue: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTextSelected: (String) -> Unit,
    textValue: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    errorStatus: Boolean = false,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    val unfocusedBorderColor =
        if (errorStatus && isFocused) Color.Green else MaterialTheme.colorScheme.secondary

    OutlinedTextField(
        value = textValue,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,        // cuando está enfocado
            unfocusedBorderColor = Color(0xFFE6E6E6), // cuando no está enfocado
            errorBorderColor = Color.Red,
            unfocusedLabelColor = Color(0xFFE6E6E6),
            unfocusedLeadingIconColor = Color(0xFFE6E6E6)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.onFocusEvent {
            if (it.isFocused) {
                // Notificamos al padre que perdió el foco
                isFocused = true
            }
        },
        label = {
            Text(
                text = labelValue,
                fontWeight = FontWeight.Medium
            )
        },
        keyboardOptions = keyboardOptions,
        singleLine = true,
        maxLines = 1,
        onValueChange = {
            onTextSelected(it)
        },
        leadingIcon = leadingIcon?.let { nonNullIcon ->
            { Icon(nonNullIcon, contentDescription = null) }
        },
        trailingIcon = trailingIcon?.let { nonNullIcon ->
            { Icon(nonNullIcon, contentDescription = null) }
        },
        isError = if (isFocused) errorStatus else false
    )
}
