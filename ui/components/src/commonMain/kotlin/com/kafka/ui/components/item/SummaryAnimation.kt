package com.kafka.ui.components.item

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
expect fun SummaryAnimation(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
)
