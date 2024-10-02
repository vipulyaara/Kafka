package org.kafka.common.adaptive

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
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
