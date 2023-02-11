package org.kafka.common

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val WIDE_LAYOUT_MIN_WIDTH = 600.dp

@Composable
fun BoxWithConstraintsScope.isWideLayout() = maxWidth >= WIDE_LAYOUT_MIN_WIDTH
