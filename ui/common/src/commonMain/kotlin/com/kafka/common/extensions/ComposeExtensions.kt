@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.common.extensions

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

fun TextStyle.alignCenter() = merge(TextStyle(textAlign = TextAlign.Center))

fun TextStyle.medium() = merge(TextStyle(fontWeight = FontWeight.Medium))

fun TextStyle.semiBold() = merge(TextStyle(fontWeight = FontWeight.SemiBold))

@Composable
inline fun <T> rememberMutableState(
    key: Any? = null,
    crossinline init: @DisallowComposableCalls () -> T,
) = remember(key) { mutableStateOf(init()) }

@Composable
inline fun <T> rememberSavableMutableState(
    key: Any? = null,
    saver: Saver<T, out Any> = autoSaver(),
    crossinline init: @DisallowComposableCalls () -> T,
) = rememberSaveable(key, saver) { mutableStateOf(init()) }

@Composable
fun AnimatedVisibilityFade(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    androidx.compose.animation.AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = enter,
        exit = exit,
        content = content
    )
}

@Composable
fun ProvideInteractiveEnforcement(
    enabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        value = LocalMinimumInteractiveComponentEnforcement provides enabled,
        content = content
    )
}

val MinimumInteractiveComponentSize = 48.dp
