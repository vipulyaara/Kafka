package org.kafka.common.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Delays visibility of given [content] for [delayMillis].
 */
@Composable
fun Delayed(
    modifier: Modifier = Modifier,
    delayMillis: Long = 500,
    content: @Composable () -> Unit
) {
    TimedVisibility(
        delayMillis = delayMillis,
        visibility = false,
        modifier = modifier,
        content = content
    )
}

/**
 * Changes visibility of given [content] after [delayMillis] to opposite of initial [visibility].
 */
@Composable
fun TimedVisibility(
    modifier: Modifier = Modifier,
    delayMillis: Long = 4000,
    visibility: Boolean = true,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(visibility) }
    val coroutine = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val job = coroutine.launch {
            delay(delayMillis)
            visible = !visible
        }

        onDispose {
            job.cancel()
        }
    }
    AnimatedVisibility(visible = visible, modifier = modifier, enter = fadeIn(), exit = fadeOut()) {
        content()
    }
}
