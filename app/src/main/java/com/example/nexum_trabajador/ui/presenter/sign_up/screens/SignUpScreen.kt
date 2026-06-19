package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.ui.components.ButtonComponent
import com.example.nexum_trabajador.ui.components.DividerTextComponent
import com.example.nexum_trabajador.ui.components.MyDialog2
import com.example.nexum_trabajador.ui.components.MyTextFieldComponent
import com.example.nexum_trabajador.ui.components.NexumLogo
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpState
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpViewModel
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 05/08/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {},
) {
    val state = viewModel.state

    SignUpScreenContent(
        state = state,
        emailChanged = {
            viewModel.onEvent(SignUpEvent.EmailChanged(it))
        },
        onContinueButtonClicked = {
            go(AuthRoutes.SignUpUserDataScreen)
        },
        onSignInTextClicked = {
            go(AuthRoutes.SignInScreen())
        },
        onPolicyClicked = {
            viewModel.onEvent(SignUpEvent.ShowPolicyDialog)
//            go(AuthRoutes.SignUpPolicyScreen)
        },
        onTosClicked = {
            viewModel.onEvent(SignUpEvent.ShowTosDialog)
//            go(AuthRoutes.SignUpTosScreen)
        },
    )

    TosDialog(state, viewModel)

    PolicyDialog(state, viewModel)

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PolicyDialog(
    state: SignUpState,
    viewModel: SignUpViewModel
) {
    MyDialog2(
        title = "Política de privacidad",
        show = state.showPolicyDialog,
        onDismiss = {
            viewModel.onEvent(SignUpEvent.DismissPolicyDialog)
        },
        onConfirm = {
            viewModel.onEvent(SignUpEvent.ConfirmPolicyDialog)
        }
    ) {
        SignUpPolicyScreen()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TosDialog(
    state: SignUpState,
    viewModel: SignUpViewModel
) {
    MyDialog2(
        title = "Términos de uso",
        show = state.showTosDialog,
        onDismiss = {
            viewModel.onEvent(SignUpEvent.DismissTosDialog)
        },
        onConfirm = {
            viewModel.onEvent(SignUpEvent.ConfirmTosDialog)
        }
    ) {
        SignUpTosScreen()
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun SignUpScreenContent(
    state: SignUpState = SignUpState(),
    emailChanged: (String) -> Unit = {},
    onContinueButtonClicked: () -> Unit = {},
    onTosClicked: () -> Unit = {},
    onPolicyClicked: () -> Unit = {},
    onSignInTextClicked: () -> Unit = {},
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
                    "Crea tu cuenta",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
                Text("Ingrese su correo para registrarse", fontSize = 18.sp)

                MyTextFieldComponent(
                    labelValue = "Ingrese su correo",
                    textValue = state.email,
                    leadingIcon = Icons.Default.Email,
                    onTextSelected = {
                        emailChanged(it)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    errorStatus = state.emailError
                )

                ButtonComponent(
                    value = "Continuar",
                    isEnabled = state.isValidToContinue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Color.White,
                        disabledContentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    onContinueButtonClicked()
                }
                DividerTextComponent()

                TosAndPolicyText(
                    tosClicked = onTosClicked,
                    policyClicked = onPolicyClicked
                )

                SignInText(onSignInTextClicked)

            }
        }
    }
}


@Composable
private fun TosAndPolicyText(
    tosClicked: () -> Unit = {},
    policyClicked: () -> Unit = {}
) {
    val initTxt = "Al hacer clic en continuar, aceptas nuestros "
    val tosTxt = "Términos de servicio"
    val policyTxt = "Política de privacidad."

    val annotatedString = buildAnnotatedString {
        append(initTxt)
        withStyle(style = SpanStyle(color = Color(0xFF828282))) {
            pushStringAnnotation(tag = tosTxt, annotation = tosTxt)
            append(tosTxt)
        }
        append(" y ")
        withStyle(style = SpanStyle(color = Color(0xFF828282))) {
            pushStringAnnotation(tag = policyTxt, annotation = policyTxt)
            append(policyTxt)
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
                    if ((span.item == tosTxt)) {
                        tosClicked()
                    }
                    if ((span.item == policyTxt)) {
                        policyClicked()
                    }
                }
        }
    )
}

@Composable
private fun SignInText(go: () -> Unit) {
    val initTxt = "¿Ya tienes una cuenta?  "
    val signUpTxt = "Inicia sesión"

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
                        //TODO Navegar al inicio de sesión
                        go()
                    }
                }
        }
    )
}
