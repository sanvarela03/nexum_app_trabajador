package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nexum_trabajador.common.capitalizeFirstLetter
import com.example.nexum_trabajador.domain.model.MarketLocation


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CitiesComponent(
    selectedMarketLocation: MarketLocation? = null,
    marketLocations: List<MarketLocation> = emptyList(),
    onCitySelected: (MarketLocation) -> Unit = {},
    maxHeight: Dp = 340.dp
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)      // <- cap height
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE6E6E6), RoundedCornerShape(12.dp)),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(marketLocations) { marketLocation ->
            CityItem(
                text = marketLocation.city.capitalizeFirstLetter(),
                isSelected = selectedMarketLocation?.id == marketLocation.id,
                onClick = {
                    onCitySelected(marketLocation)
                }
            )
        }
    }
}
