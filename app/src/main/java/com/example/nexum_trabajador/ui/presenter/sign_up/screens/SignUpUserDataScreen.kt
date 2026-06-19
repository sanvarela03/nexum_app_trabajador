package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.ui.components.MyTextFieldComponent
import com.example.nexum_trabajador.ui.components.PagerNavigationComponent
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpState
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpViewModel
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme

@Composable
fun SignUpUserDataScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {}
) {
    val state = viewModel.state

    val textValue = rememberSaveable { mutableStateOf("") }
    Log.d("TAG", "SignUpUserDataScreen: ${state.email}")
    SignUpUserDataScreenContent(
        state = state,
        nameChanged = {
            viewModel.onEvent(SignUpEvent.NameChanged(it))
        },
        lastNameChanged = {
            viewModel.onEvent(SignUpEvent.LastNameChanged(it))
        },
        onNextClicked = {
            go(AuthRoutes.SignUpCellphoneScreen)
        },
        onBackClicked = {
            go(AuthRoutes.SignUpScreen)
        },
        enableNextButton = state.isUserDataValid

    )

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SignUpUserDataScreenContent(
    state: SignUpState = SignUpState(),
    nameChanged: (String) -> Unit = {},
    lastNameChanged: (String) -> Unit = {},
    onNextClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    enableNextButton: Boolean = false,
) {
    Nexum_trabajadorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    top = 80.dp,    // ↑ increase this value
                    end = 20.dp,
                    bottom = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )
        {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                Text(
                    "¿Cómo te llamas?",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
                Text("Dinos cómo debemos llamarte \uD83D\uDE0A", fontSize = 14.sp)

                Text(
                    "Nombre",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                MyTextFieldComponent(
                    labelValue = "Ingrese su nombre",
                    textValue = state.name,
                    onTextSelected = {
                        nameChanged(it)
//                    viewModel.onEvent(SignUpEvent.NameChanged(it))
                    },
                    errorStatus = state.nameError
                )
                Text(
                    "Apellido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                MyTextFieldComponent(
                    labelValue = "Ingrese su apellido",
                    textValue = state.lastName,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onTextSelected = {
                        lastNameChanged(it)
//                    viewModel.onEvent(SignUpEvent.LastNameChanged(it))
                    },
                    errorStatus = state.lastNameError
                )
                Spacer(modifier = Modifier.weight(1f))
                PagerNavigationComponent(
                    onBack = {
                        onBackClicked()
//                    go(AuthRoutes.SignUpScreen)
                    },
                    onNext = {
                        onNextClicked()
//                    go(AuthRoutes.SignUpCellphoneScreen)
                    },
//                enableNextButton = viewModel.state.nameError && viewModel.state.lastNameError
                    enableNextButton = enableNextButton
                )
            }
        }
    }
}
