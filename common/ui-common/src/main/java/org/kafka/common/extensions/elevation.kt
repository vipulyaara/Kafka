package org.kafka.common.extensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.unit.dp

fun ScrollState?.elevation(maxElevation: Int = 40) = this?.run {
    if (maxValue != 0) (value.toFloat() / maxValue.toFloat()) * maxElevation
    else 0f
}?.dp ?: 0.dp

fun LazyListState?.elevation(maxElevation: Int = 40, step: (Int) -> Int = { it }) = this?.run {
    step(firstVisibleItemScrollOffset).coerceAtMost(maxElevation)
}?.dp ?: 0.dp

fun LazyGridState?.elevation(maxElevation: Int = 40, step: (Int) -> Int = { it }) = this?.run {
    step(firstVisibleItemScrollOffset).coerceAtMost(maxElevation)
}?.dp ?: 0.dp
