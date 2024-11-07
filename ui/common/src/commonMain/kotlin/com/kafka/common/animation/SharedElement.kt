@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.kafka.common.animation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.kafka.base.debug

val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("No SharedTransitionScope provided")
}

val LocalAnimatedContentScope = staticCompositionLocalOf<AnimatedContentScope> {
    error("No AnimatedContentScope provided")
}

@Composable
fun ProvideLocalAnimatedContentScope(
    animatedContentScope: AnimatedContentScope,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAnimatedContentScope provides animatedContentScope, content)
}

@Composable
fun coverImageKey(url: Any?): String {
    debug { "Shared key is $url" }
    return remember(url) { "cover_$url" }
}
