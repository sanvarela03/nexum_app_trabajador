package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.ui.components.MyNumberFieldComponent
import com.example.nexum_trabajador.ui.components.PagerNavigationComponent
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
@Composable
fun SignUpIDScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {},
    onBack: () -> Boolean
) {
    SignUpIDScreenContent(
        state = viewModel.state,
        onDocumentNumberChanged = {
            viewModel.onEvent(SignUpEvent.DocumentNumberChanged(it))
        },
        onBack = { onBack() },
        onNext = { go(AuthRoutes.SignUpUploadDocumentScreen) },
        enableNextButton = viewModel.state.isIdValid
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SignUpIDScreenContent(
    state: SignUpState = SignUpState(),
    onDocumentNumberChanged: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    enableNextButton: Boolean = false
) {
    Nexum_trabajadorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    top = 80.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "¿Cuál es tu n° de documento de identidad?",
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(10.dp))
            TosAndPolicyText { }
            Text(
                "Número de documento",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            MyNumberFieldComponent(
                modifier = Modifier
                    .fillMaxWidth(),
                labelValue = "Ingrese su n° de documento",
                numberValue = state.documentNumber,
                keyBoardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onTextSelected = {
                    onDocumentNumberChanged(it)
                },
                errorStatus = state.documentNumberError,
                focusHops = 2
            )
            Spacer(modifier = Modifier.weight(1f))
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

@Composable
private fun TosAndPolicyText(go: () -> Unit) {
    val initTxt =
        "Solicitamos esta información para asegurar nuestras operaciones, consulta nuestros "
    val signUpTxt = "términos de uso"
    val policyTxt = "política de privacidad."

    val annotatedString = buildAnnotatedString {
        append(initTxt)
        withStyle(style = SpanStyle(color = Color(0xFF828282))) {
            pushStringAnnotation(tag = signUpTxt, annotation = signUpTxt)
            append(signUpTxt)
        }
        append(" y ")
        withStyle(style = SpanStyle(color = Color(0xFF828282))) {
            pushStringAnnotation(tag = policyTxt, annotation = policyTxt)
            append(policyTxt)
        }
    }

    Text(
        text = annotatedString,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Start,
        color = Color(0xFF000000)
    )
}
