package com.example.nexum_trabajador.ui.presenter.sign_in

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nexum_trabajador.ui.components.ButtonComponent
import com.example.nexum_trabajador.ui.components.DividerTextComponent
import com.example.nexum_trabajador.ui.components.MyTextFieldComponent
import com.example.nexum_trabajador.ui.components.NexumLogo
import com.example.nexum_trabajador.ui.components.PasswordTextFieldComponent
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme

@Composable
fun SignInScreen(
    username: String? = null,
    viewModel: SignInViewModel = hiltViewModel(),
    go: (Any) -> Unit = {},
) {
    val state = viewModel.state
    if (username != null) {
        Log.d("SignInScreen", "Username: $username")
        viewModel.onEvent(SignInEvent.UsernameChanged(username))
    }

    Nexum_trabajadorTheme {
        SignInScreenContent(
            state = state,
            emailChanged = {
                viewModel.onEvent(SignInEvent.UsernameChanged(it))
            },
            passwordChanged = {
                viewModel.onEvent(SignInEvent.PasswordChanged(it))
            },
            onLoginButtonClicked = {
                viewModel.onEvent(SignInEvent.LoginButtonClicked)
            },
            onForgotPasswordClicked = {
                viewModel.onEvent(SignInEvent.ForgotPasswordButtonClicked)
            },
            go = {
                go(AuthRoutes.SignUpScreen)
            }
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SignInScreenContent(
    state: SignInState = SignInState(),
    emailChanged: (String) -> Unit = {},
    passwordChanged: (String) -> Unit = {},
    onLoginButtonClicked: () -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    go: () -> Unit = {}
) {
    Nexum_trabajadorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            )
            {
                NexumLogo()
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Ingresar a tu cuenta",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
                Text("Ingrese su correo para iniciar sesión", fontSize = 18.sp)

                MyTextFieldComponent(
                    labelValue = "Ingrese su correo",
                    textValue = state.email,
                    leadingIcon = Icons.Default.Email,
                    onTextSelected = {
                        emailChanged(it)
//                    viewModel.onEvent(SignInEvent.UsernameChanged(it))
                    },
                    errorStatus = state.emailError,
                )
                PasswordTextFieldComponent(
                    labelValue = "Ingrese su contraseña",
                    password = state.password,
                    icon = Icons.Default.Lock,
                    onTextSelected = {
                        passwordChanged(it)
                    },
                    errorStatus = state.passwordError
                )
                ForgotPasswordText(onForgotPasswordClicked)
                ButtonComponent(
                    value = "Ingresar",
                    isEnabled = state.isButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Color.White,
                        disabledContentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    onLoginButtonClicked()
//                viewModel.onEvent(SignInEvent.LoginButtonClicked)
                }
                DividerTextComponent()


                SignUpText(go)
            }
        }

    }
}


@Composable
private fun ForgotPasswordText(
//    viewModel: SignInViewModel,
    onForgotPasswordClicked: () -> Unit = {}
) {
    ClickableText(
        text = buildAnnotatedString { append("¿Olvidaste tu contraseña?") },
        style = TextStyle(
            fontSize = 14.sp,
            textAlign = TextAlign.Right,
            textDecoration = TextDecoration.Underline,
            color = Color(0xFF828282)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        onClick = {
            onForgotPasswordClicked()
//            viewModel.onEvent(SignInEvent.ForgotPasswordButtonClicked)
        }
    )
}

@Composable
private fun SignUpText(go: () -> Unit) {
    val initTxt = "¿No tienes una cuenta?  "
    val signUpTxt = "Registrarse"

    val annotatedString = buildAnnotatedString {
        append(initTxt)
        withStyle(style = SpanStyle(color = Color(0xFF828282))) {
            pushStringAnnotation(tag = signUpTxt, annotation = signUpTxt)
            append(signUpTxt)
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF000000)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset).firstOrNull()
                ?.also { span ->
                    if ((span.item == signUpTxt)) {
                        //TODO Navegar al registro
//                        go(AuthRoutes.SignUpScreen)
                        go()
                    }
                }
        }
    )
}
