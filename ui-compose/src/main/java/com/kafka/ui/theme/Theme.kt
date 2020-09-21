package com.kafka.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = AppColorsLight,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
