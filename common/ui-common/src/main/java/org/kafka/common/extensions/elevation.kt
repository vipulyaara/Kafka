package org.kafka.common.extensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.unit.dp

val ScrollState.elevation
    get() = derivedStateOf {
        this.run { ((value / 100) * 2).coerceAtMost(MaxElevation).dp } ?: 0.dp
    }

val LazyListState.elevation
    get() = this.run {
        if (firstVisibleItemIndex == 0) {
            minOf(firstVisibleItemScrollOffset.toFloat().dp, MaxElevation.dp)
        } else {
            20.dp
        }
    }

fun LazyGridState.elevation(maxElevation: Int = 40) = derivedStateOf {
    this.run { firstVisibleItemScrollOffset.coerceAtMost(maxElevation) }.dp
}

private const val MaxElevation = 20
