package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.domain.model.Country
import com.example.nexum_trabajador.domain.model.MarketLocation
import com.example.nexum_trabajador.ui.components.CitiesComponent
import com.example.nexum_trabajador.ui.components.PagerNavigationComponent
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpState
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpViewModel

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 19/08/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpCityScreen(
    viewModel: SignUpViewModel,
    go: (Any) -> Unit = {}
) {
    val state = viewModel.state

    SignUpCityScreenContent(
        state = state,
        onCityChanged = {
            if (it.id != state.selectedMarketLocation?.id) {
                viewModel.onEvent(SignUpEvent.CityChanged(it))
            } else {
                viewModel.onEvent(SignUpEvent.CityChanged(null))
            }
        },
        onBack = {
            go(AuthRoutes.SignUpBirthdayScreen)
        },
        onNext = {
            go(AuthRoutes.SignUpIDScreen)
        },
        enableNextButton = state.isCityValid
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SignUpCityScreenContent(
    state: SignUpState = SignUpState(
        marketLocations = listOf(
            MarketLocation(
                id = 1,
                city = "Bogotá",
                state = "Cundinamarca",
                country = "Colombia",
                countryCode = "CO"
            )
        ),
        selectedCountry = Country(
            id = 1,
            name = "Colombia",
            code = "CO",
            phoneCode = "+57",
            flagEmoji = "🇨🇴",
            phoneCheckRegex = "[0-9]{7,10}"
        ),
    ),
    onCityChanged: (MarketLocation) -> Unit = {},
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    enableNextButton: Boolean = false,
) {
    val cities = state.marketLocations.filter { it.countryCode == state.selectedCountry?.code }
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
            "¿Cuál es tu ciudad?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            textAlign = TextAlign.Start
        )
        Text(
            "Selecciona la ciudad en la que quieres solicitar nuestros servicios.",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color(0xFF858191),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(35.dp))
        CitiesComponent(
            selectedMarketLocation = state.selectedMarketLocation,
            marketLocations = cities,
            onCitySelected = {
                onCityChanged(it)
            },
            maxHeight = 400.dp
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
