package com.kafka.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LazyListState.elevation: Dp
    get() = if (firstVisibleItemIndex == 0) {
        // For the first element, use the minimum of scroll offset and default elevation
        // i.e. a value between 0 and 4.dp
        minOf(firstVisibleItemScrollOffset.toFloat().dp, 24.dp)
    } else {
        // If not the first element, always set elevation and show the shadow
        24.dp
    }

val LazyGridState.elevation: Dp
    get() = if (firstVisibleItemIndex == 0) {
        // For the first element, use the minimum of scroll offset and default elevation
        minOf(firstVisibleItemScrollOffset.toFloat().dp, 24.dp)
    } else {
        // If not the first element, always set elevation and show the shadow
        24.dp
    }
