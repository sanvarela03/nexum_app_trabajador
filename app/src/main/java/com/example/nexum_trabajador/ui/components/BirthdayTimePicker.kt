package com.example.nexum_trabajador.ui.components

// ---------- Imports ----------
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nexum_trabajador.common.capitalizeFirstLetter
import java.util.Calendar
import java.util.TimeZone

// ---------- Utils (API-24 safe) ----------
private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

private fun daysInMonth(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (isLeapYear(year)) 29 else 28
    else -> 30
}

// ---------- Public API ----------
@Composable
fun DateOfBirthPicker(
    modifier: Modifier = Modifier,
    // integers instead of LocalDate to avoid API-26 requirement
    initialYear: Int = 1990,
    initialMonth: Int = 1, // 1..12
    initialDay: Int = 1,   // 1..31 (will be coerced)
    minYear: Int = 1900,
    maxYear: Int = Calendar.getInstance().get(Calendar.YEAR),
//    onDateChanged: (year: Int, month: Int, day: Int) -> Unit = {},
    onDayChanged: (day: Int) -> Unit,
    onMonthChanged: (month: Int) -> Unit,
    onYearChanged: (year: Int) -> Unit,
    timeZone: String = "GMT-5"
) {
    val tz = TimeZone.getTimeZone(timeZone)
    val now = Calendar.getInstance(tz)

    var selectedYear by rememberSaveable {
        mutableIntStateOf(
            initialYear.coerceIn(
                minYear,
                maxYear
            )
        )
    }
    var selectedMonth by rememberSaveable(selectedYear) {
        mutableIntStateOf(
            initialMonth.coerceIn(
                1,
                if (selectedYear == maxYear) now.get(Calendar.MONTH) + 1 else 12
            )
        )
    }
    var selectedDay by rememberSaveable(
        selectedYear,
        selectedMonth
    ) {
        mutableIntStateOf(
            initialDay.coerceIn(
                1,
                if (selectedYear == maxYear && selectedMonth == now.get(Calendar.MONTH) + 1) now.get(
                    Calendar.DAY_OF_MONTH
                ) else 31
            )
        )
    }

    // Re-coerce the day whenever year/month changes
    var maxDay by remember { mutableIntStateOf(daysInMonth(selectedYear, selectedMonth)) }

    Log.d("maxDay", "maxDay: $maxDay")
    if (selectedDay > maxDay) selectedDay = maxDay

    // Notify parent on any change
    LaunchedEffect(selectedYear) {
        maxDay = daysInMonth(selectedYear, selectedMonth)
        onYearChanged(selectedYear)
    }
    LaunchedEffect(selectedMonth) {
        maxDay = daysInMonth(selectedYear, selectedMonth)
        onMonthChanged(selectedMonth)
    }
    LaunchedEffect(selectedDay) {
        if (selectedDay > maxDay) selectedDay = maxDay
        onDayChanged(selectedDay)
    }

//    LaunchedEffect(selectedYear, selectedMonth, selectedDay) {
//        maxDay = daysInMonth(selectedYear, selectedMonth)
//        if (selectedDay > maxDay) selectedDay = maxDay
//        onDateChanged(selectedYear, selectedMonth, selectedDay)
//        Log.d("DateOfBirthPicker", "onDateChanged: $selectedYear-$selectedMonth-$selectedDay")
//        Log.d("DateOfBirthPicker", "maxDay: $maxDay")
//        Log.d("DateOfBirthPicker", "selectedDay: $selectedDay")
//        Log.d("DateOfBirthPicker", "selectedMonth: $selectedMonth")
//    }

    val borderColor = Color(0xFFE6E6E6)

    Box(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        // center highlight band (like iOS/your screenshot)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF2F2F2))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val daysArray =
                remember(maxDay, selectedYear, selectedMonth) { // <--- AÑADE maxDay como key aquí
                    val maxDayOfArray =
                        if (selectedYear == maxYear && selectedMonth == now.get(Calendar.MONTH) + 1) now.get(
                            Calendar.DAY_OF_MONTH
                        ) else maxDay
                    val initialDays = (1..maxDayOfArray).map { it.toString().padStart(2, '0') }
                    mutableStateListOf<String>().apply { addAll(initialDays) }
                }

            // DAY (fixed width)
            WheelPicker(
                items = daysArray,
                initialIndex = (selectedDay - 1),
                onSelectedIndex = { selectedDay = it + 1 },
                modifier = Modifier.width(70.dp)
            )

            // MONTH (takes remaining space via weight)
            val monthLabels = remember(selectedYear) {
                val monthsArray = mutableStateListOf(
                    "enero", "febrero", "marzo", "abril", "mayo", "junio",
                    "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
                ).subList(0, if (selectedYear == maxYear) now.get(Calendar.MONTH) + 1 else 12)

                mutableStateListOf<String>().apply { addAll(monthsArray.map { it.capitalizeFirstLetter() }) }
            }
            WheelPicker(
                items = monthLabels,
                initialIndex = (selectedMonth - 1),
                onSelectedIndex = { selectedMonth = it + 1 },
                modifier = Modifier.weight(1f) // ✅ works because inside Row scope + import
            )

            // YEAR (fixed width)
            val yearsArray = remember {
                mutableStateListOf<String>().apply { addAll((minYear..maxYear).map { it.toString() }) }
            }
            WheelPicker(
                items = yearsArray,
                initialIndex = yearsArray.indexOf(selectedYear.toString()).coerceAtLeast(0),
                onSelectedIndex = { centeredIndex ->
                    selectedYear = yearsArray[centeredIndex].toInt()
                },
                modifier = Modifier.width(90.dp)
            )
        }
    }
}
