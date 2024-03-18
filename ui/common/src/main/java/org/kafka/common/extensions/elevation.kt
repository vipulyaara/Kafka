package org.kafka.common.extensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.Dimens

val ScrollState.elevation
    get() = derivedStateOf {
        this.run { ((value / 100) * 2).coerceAtMost(MaxElevation).dp } ?: 0.dp
    }

val LazyListState.elevation: Dp
    get() = if (firstVisibleItemIndex == 0) {
        // For the first element, use the minimum of scroll offset and default elevation
        // i.e. a value between 0 and 4.dp
        minOf(firstVisibleItemScrollOffset.toFloat().dp, 24.dp)
    } else {
        // If not the first element, always set elevation and show the shadow
        24.dp
    }


val LazyListState?.elevation
    get() = derivedStateOf {
        if (this?.firstVisibleItemIndex == 0) {
            minOf(firstVisibleItemScrollOffset.toFloat().dp, MaxElevation.dp)
        } else {
            Dimens.Spacing20
        }
    }


fun LazyGridState.elevation(maxElevation: Int = 40) = derivedStateOf {
    this.run { firstVisibleItemScrollOffset.coerceAtMost(maxElevation) }.dp
}

private const val MaxElevation = 20
