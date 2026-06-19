package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.common.formatToDateOnly
import com.example.nexum_trabajador.common.toLocalDateTime
import com.example.nexum_trabajador.ui.components.DateOfBirthPicker
import com.example.nexum_trabajador.ui.components.PagerNavigationComponent
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpState
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpViewModel
import java.util.Calendar

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 24/07/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpBirthDateScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {}
) {
    SignUpBirthDateScreenContent(
        state = viewModel.state,
        onDayChanged = {
            viewModel.onEvent(SignUpEvent.BirthDateDayChanged(it))
        },
        onMonthChanged = {
            viewModel.onEvent(SignUpEvent.BirthDateMonthChanged(it))
        },
        onyYearChanged = {
            viewModel.onEvent(SignUpEvent.BirthDateYearChanged(it))
        },
        onNextClicked = {
            go(AuthRoutes.SignUpCityScreen)
        },
        onBackClicked = {
            go(AuthRoutes.SignUpUserDataScreen)
        },
        enableNextButton = viewModel.state.isBirthDateValid
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SignUpBirthDateScreenContent(
    state: SignUpState = SignUpState(),
    onDayChanged: (Int) -> Unit = {},
    onMonthChanged: (Int) -> Unit = {},
    onyYearChanged: (Int) -> Unit = {},
    onNextClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    enableNextButton: Boolean = false,
) {
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
    ) {
        Text(
            "¿Cuál es tu fecha de nacimiento?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(35.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        )
        {
            Text(
                "Fecha de nacimiento",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Spacer(Modifier.height(8.dp))

            DateOfBirthPicker(
                initialYear = state.birthDateYear,
                initialMonth = state.birthDateMonth,
                initialDay = state.birthDateDay,
                minYear = 1925,
                maxYear = Calendar.getInstance().get(Calendar.YEAR) - 18,
                onDayChanged = {
                    onDayChanged(it)
                },
                onMonthChanged = {
                    onMonthChanged(it)
                },
                onYearChanged = {
                    onyYearChanged(it)
                }
            )

            val selectedDate = "%04d-%02d-%02d".format(
                state.birthDateYear,
                state.birthDateMonth,
                state.birthDateDay
            )

            val date = selectedDate.toLocalDateTime().formatToDateOnly()
            Row {
                Text("Seleccionado: ")
                Text(
                    "${date}",
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        PagerNavigationComponent(
            onBack = {
                onBackClicked()
            },
            onNext = {
                onNextClicked()
            },
            enableNextButton = enableNextButton
        )
    }
}
