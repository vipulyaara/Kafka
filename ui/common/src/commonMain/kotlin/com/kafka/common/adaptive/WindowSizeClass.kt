package com.kafka.common.adaptive

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun windowSizeClass() = currentWindowAdaptiveInfo().windowSizeClass

@Composable
fun windowWidthSizeClass() = windowSizeClass().windowWidthSizeClass

@Composable
fun isCompactWidth() = windowWidthSizeClass().isCompact()

@Composable
fun isMediumWidth() = windowWidthSizeClass().isMedium()

@Composable
fun isExpandedWidth() = windowWidthSizeClass().isExpanded()

fun WindowWidthSizeClass.isCompact() = this == WindowWidthSizeClass.COMPACT
fun WindowWidthSizeClass.isNotCompact() = this != WindowWidthSizeClass.COMPACT
fun WindowWidthSizeClass.isMedium() = this == WindowWidthSizeClass.MEDIUM
fun WindowWidthSizeClass.isExpanded() = this == WindowWidthSizeClass.EXPANDED
fun WindowWidthSizeClass.useWideLayout() = isExpanded()

/**
 * Identifies grid cells.
 * It uses a fixed number of cells for compact (mobile) and adaptive sizing otherwise.
 * */
@Composable
fun gridColumns(fixedColumns: Int, adaptiveWidth: WindowWidth) =
    if (windowWidthSizeClass().isCompact()) {
        GridCells.Fixed(fixedColumns)
    } else {
        GridCells.Adaptive(adaptiveWidth.width)
    }

enum class WindowWidth(val width: Dp) {
    Small(260.dp), Medium(360.dp), Large(460.dp), XLarge(560.dp)
}
