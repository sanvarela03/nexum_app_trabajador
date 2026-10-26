package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.ui.components.PagerNavigationComponent
import com.example.nexum_trabajador.ui.components.PasswordTextFieldComponent
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpState
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpViewModel
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 20/08/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpPasswordScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {},
    onBack: () -> Boolean
) {
    val state = viewModel.state

    SignUpPasswordScreenContent(
        state = state,
        onPasswordChanged = { viewModel.onEvent(SignUpEvent.PasswordChanged(it)) },
        onConfirmPasswordChanged = { viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged(it)) },
        onBack = { onBack() },
        onNext = { go(AuthRoutes.SignUpProfilePictureScreen) },
        enableNextButton = state.isPasswordValid
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SignUpPasswordScreenContent(
    state: SignUpState = SignUpState(),
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    enableNextButton: Boolean = false,
) {
    Nexum_trabajadorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 10.dp,
                    top = 80.dp,
                    end = 10.dp,
                    bottom = 20.dp
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "Elige una contraseña segura",
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                textAlign = TextAlign.Start
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    "Contraseña",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                PasswordTextFieldComponent(
                    labelValue = "Ingrese su contraseña",
                    password = state.password,
                    icon = Icons.Default.Lock,
                    onTextSelected = {
                        onPasswordChanged(it)
                    },
                    errorStatus = state.passwordError
                )
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    "Confirmar contraseña",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                PasswordTextFieldComponent(
                    labelValue = "Confirme su contraseña",
                    password = state.confirmPassword,
                    icon = Icons.Default.Lock,
                    onTextSelected = {
                        onConfirmPasswordChanged(it)
                    },
                    errorStatus = state.confirmPasswordError
                )
            }
            PagerNavigationComponent(
                onBack = {
                    onBack()
                },
                onNext = {
                    onNext()
                },
                enableNextButton = enableNextButton
            )
        }
    }
}
