package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.domain.model.Country
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
 * @since 24/07/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpCellphoneScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {},
    onBack: () -> Boolean
) {
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.onEvent(SignUpEvent.LoadCountries(fetchFromRemote = true))
    }

    SignUpCellphoneScreenContent(
        state = state,
        cellphoneChanged = {
            viewModel.onEvent(SignUpEvent.CellphoneChanged(it))
        },
        phoneCodeChanged = {
            viewModel.onEvent(SignUpEvent.PhoneCodeChanged(it))
        },
        onBack = {
            onBack()
        },
        onNext = {
            go(AuthRoutes.SignUpBirthdayScreen)
        },
        enableNextButton = state.isPhoneValid
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun SignUpCellphoneScreenContent(
    state: SignUpState = SignUpState(),
    cellphoneChanged: (String) -> Unit = {},
    phoneCodeChanged: (Country?) -> Unit = {},
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    enableNextButton: Boolean = false,
) {
    Nexum_trabajadorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = 80.dp,
                    end = 16.dp,
                    bottom = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "¿Cuál es tu número de teléfono?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    "Celular",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Row(
                    modifier = Modifier
                        .padding(0.dp)
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        PhoneCodePicker(
                            state = state,
                            phoneCodeChanged = phoneCodeChanged,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f),
                    ) {
                        MyNumberFieldComponent(
                            modifier = Modifier
                                .padding(vertical = 0.dp)
                                .fillMaxWidth()
                                .height(68.dp),
                            keyBoardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            shape = RoundedCornerShape(12.dp),
                            labelValue = "Ingresar tu número",
                            numberValue = state.phone,
                            onTextSelected = cellphoneChanged,
                            errorStatus = state.phoneError,
                            focusHops = 2
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                PagerNavigationComponent(
                    onBack = onBack,
                    onNext = onNext,
                    enableNextButton = enableNextButton
                )
            }
        }
    }
}

@Composable
private fun PhoneCodePicker(
    state: SignUpState,
    phoneCodeChanged: (Country?) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val countries = state.countries

    var displayText by rememberSaveable(state.selectedCountry) {
        mutableStateOf(
            if (state.selectedCountry == null) "Código"
            else "${state.selectedCountry?.flagEmoji ?: ""} ${state.selectedCountry?.phoneCode ?: ""}"
        )
    }

    Box(
        modifier = Modifier
            .height(60.dp)
            .border(
                1.dp,
                if (state.phoneCodeError) Color.Red else Color(0xFFE6E6E6),
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { expanded = !expanded },
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = displayText,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f),
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "More options"
            )
            DropdownMenu(
                expanded = expanded,
                modifier = Modifier.background(Color.White),
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text("${country.flagEmoji ?: ""} ${country.phoneCode ?: ""}") },
                        onClick = {
                            displayText = "${country.flagEmoji ?: ""} ${country.phoneCode ?: ""}"
                            phoneCodeChanged(country)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
