package com.example.nexum_trabajador.ui.presenter.landing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nexum_trabajador.common.capitalizeFirstLetter
import com.example.nexum_trabajador.domain.model.Worker
import com.example.nexum_trabajador.ui.presenter.home.HomeViewModel
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/30/2025
 * @version 1.0
 */
@Composable
fun LandingScreen(
    homeViewModel: HomeViewModel,
) {
    val state = homeViewModel.state

    LandingScreenContent(
        worker = state.worker
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LandingScreenContent(
    worker: Worker? = null,
) {
    Nexum_trabajadorTheme() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
        ) {
//            Text(text = "LandingScreenContent")
            Text(
                modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                text = "Hola ${worker?.firstName?.capitalizeFirstLetter()} ¿Qué quieres hacer hoy?",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                letterSpacing = MaterialTheme.typography.titleLarge.letterSpacing,
                lineHeight = MaterialTheme.typography.titleLarge.lineHeight,
                textAlign = MaterialTheme.typography.titleLarge.textAlign
            )
        }
    }
}