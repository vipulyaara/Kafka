@file:Suppress("unused")

package com.kafka.common.animation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

const val yOffset = 80

val SharedXAxisEnterTransition =
    fadeIn(animationSpec = tween(210, delayMillis = 90, easing = LinearOutSlowInEasing)) +
            slideInHorizontally(
                initialOffsetX = { 100 },
                animationSpec = tween(durationMillis = 300)
            )

val SharedXAxisExitTransition =
    fadeOut(animationSpec = tween(90, easing = FastOutLinearInEasing)) +
            slideOutHorizontally(
                targetOffsetX = { -100 },
                animationSpec = tween(durationMillis = 300)
            )

val SharedYAxisEnterTransition =
    fadeIn(animationSpec = tween(210, delayMillis = 90, easing = LinearOutSlowInEasing)) +
            slideInVertically(
                initialOffsetY = { yOffset },
                animationSpec = tween(durationMillis = 300)
            )

val SharedYAxisExitTransition =
    fadeOut(animationSpec = tween(90, easing = FastOutLinearInEasing)) +
            slideOutVertically(
                targetOffsetY = { -yOffset },
                animationSpec = tween(durationMillis = 300)
            )

val SharedZAxisEnterTransition =
    fadeIn(animationSpec = tween(210, delayMillis = 90, easing = LinearOutSlowInEasing)) +
            scaleIn(initialScale = 0.8f, animationSpec = tween(durationMillis = 300))

val SharedZAxisVariantEnterTransition =
    fadeIn(animationSpec = tween(60, delayMillis = 60, easing = LinearEasing)) +
            scaleIn(initialScale = 0.8f, animationSpec = tween(durationMillis = 300))

val SharedZAxisExitTransition =
    fadeOut(animationSpec = tween(90, easing = FastOutLinearInEasing)) +
            scaleOut(targetScale = 1.1f, animationSpec = tween(durationMillis = 300))

val SharedZAxisVariantExitTransition =
    scaleOut(targetScale = 1.1f, animationSpec = tween(durationMillis = 300))

val FadeThroughEnterTransition =
    fadeIn(animationSpec = tween(210, delayMillis = 90, easing = LinearOutSlowInEasing)) +
            scaleIn(
                initialScale = 0.92f,
                animationSpec = tween(210, delayMillis = 90, easing = LinearOutSlowInEasing)
            )

val FadeThroughExitTransition = fadeOut(animationSpec = tween(90, easing = FastOutLinearInEasing))
