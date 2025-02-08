package com.kafka.ui.components.gradients

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import ui.common.theme.theme.Rose800
import ui.common.theme.theme.Slate50
import ui.common.theme.theme.Yellow500
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GradientBox(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val position by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val angle = position * 2 * PI.toFloat()
    
    Box(
        modifier
            .background(MaterialTheme.colorScheme.surface)
            .meshGradient(
                showPoints = false,
                points = listOf(
                    listOf(
                        Offset(cos(angle) * 0.5f, sin(angle) * 0.5f) to Rose800,
                        Offset(cos(angle + 2f) * 0.7f, sin(angle + 2f) * 0.7f) to Yellow500,
                        Offset(cos(angle + 4f) * 0.5f, sin(angle + 4f) * 0.5f) to Slate50,
                    ),
                    listOf(
                        Offset(cos(angle + 1f) * 0.8f, sin(angle + 1f) * 0.8f) to Yellow500,
                        Offset(cos(angle + 3f) * 0.6f, sin(angle + 3f) * 0.6f) to Rose800,
                        Offset(cos(angle + 5f) * 0.8f, sin(angle + 5f) * 0.8f) to Slate50,
                    ),
                    listOf(
                        Offset(cos(angle + 2f) * 0.6f, sin(angle + 2f) * 0.6f) to Slate50,
                        Offset(cos(angle + 4f) * 0.9f, sin(angle + 4f) * 0.9f) to Yellow500,
                        Offset(cos(angle + 6f) * 0.6f, sin(angle + 6f) * 0.6f) to Rose800,
                    ),
                    listOf(
                        Offset(cos(angle + 3f) * 0.7f, sin(angle + 3f) * 0.7f) to Rose800,
                        Offset(cos(angle + 5f) * 0.5f, sin(angle + 5f) * 0.5f) to Slate50,
                        Offset(cos(angle + 1f) * 0.7f, sin(angle + 1f) * 0.7f) to Yellow500,
                    ),
                ),
                resolutionX = 32,
                resolutionY = 32,
            )
    )
}