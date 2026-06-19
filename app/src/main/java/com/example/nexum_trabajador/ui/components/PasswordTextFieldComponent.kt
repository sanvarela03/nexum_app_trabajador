package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(
    labelValue: String,
    icon: ImageVector,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    password : String = ""
) {
    val localFocusManager = LocalFocusManager.current
    val passwordVisible = rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        modifier = Modifier
            .fillMaxWidth(),
        label = { Text(text = labelValue) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,        // cuando está enfocado
            unfocusedBorderColor = Color(0xFFE6E6E6), // cuando no está enfocado
            errorBorderColor = Color.Red,
            unfocusedLabelColor = Color(0xFFE6E6E6),
            unfocusedLeadingIconColor = Color(0xFFE6E6E6)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardActions = KeyboardActions { localFocusManager.clearFocus() },
        maxLines = 1,
        onValueChange = {
            onTextSelected(it)
        },
        leadingIcon = {  if (!passwordVisible.value) Icon(icon, contentDescription = "") else Icon(Icons.Outlined.LockOpen, contentDescription = "") },
        trailingIcon = {
            val iconImage =
                if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            var description =
                if (passwordVisible.value) "Ocultar contraseña" else "Mostrar contraseña"

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = errorStatus
    )

}
