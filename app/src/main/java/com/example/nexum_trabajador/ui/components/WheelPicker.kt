package com.example.nexum_trabajador.ui.components

import android.util.Log
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun WheelPicker(
    items: SnapshotStateList<String>,
    initialIndex: Int = 0,
    onSelectedIndex: (Int) -> Unit,
    itemHeight: Dp = 36.dp,
    visibleCount: Int = 5,
    modifier: Modifier = Modifier,
) {
//    val state = rememberLazyListState(
//        // Start roughly centered on initialIndex
//        initialFirstVisibleItemIndex = (initialIndex - visibleCount / 2)
//            .coerceIn(0, (items.lastIndex).coerceAtLeast(0))
//    )
    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex.coerceIn(0, items.lastIndex.coerceAtLeast(0))
    )
    Log.d("WheelPicker", "${state.firstVisibleItemIndex}")
    Log.d("WheelPicker", "${state.firstVisibleItemScrollOffset}")
    Log.d("WheelPicker", "${state.layoutInfo}")
    Log.d("WheelPicker", "items:${items.size}")
    val fling = rememberSnapFlingBehavior(lazyListState = state)
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    val centeredIndex by remember (items) {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2
                abs(itemCenter - viewportCenter)
            }?.index?.coerceIn(0, items.lastIndex) ?: 0
        }
    }

    // Notify when center changes
    LaunchedEffect(centeredIndex) {
        onSelectedIndex(centeredIndex)
        Log.d("centeredIndex", "centeredIndex:$centeredIndex")
        Log.d("centeredIndex", "items:${items.size}")
        Log.d("centeredIndex", "item Last Index:${items.lastIndex}")
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val sideItems = (visibleCount - 1) / 2f

        LazyColumn(
            state = state,
            flingBehavior = fling,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = itemHeight * sideItems), // ✅ Dp * Float
            userScrollEnabled = true
        ) {
            itemsIndexed(items) { index, item ->
                val distance = abs(index - centeredIndex)
                val alpha = when (distance) {
                    0 -> 1f
                    1 -> 0.6f
                    2 -> 0.35f
                    else -> 0.2f
                }
                val size = if (distance == 0) 18.sp else 16.sp
                val weight = if (distance == 0) FontWeight.SemiBold else FontWeight.Normal

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        fontSize = size,
                        fontWeight = weight,
                        color = Color.Black.copy(alpha = alpha)
                    )
                }
            }
        }
    }
}
