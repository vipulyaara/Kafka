package org.kafka.common.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kafka.common.extensions.AnimatedVisibilityFade

/**
 * Delays visibility of given [content] for [delayMillis].
 */
@Composable
fun Delayed(
    modifier: Modifier = Modifier,
    delayMillis: Long = 220,
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
    AnimatedVisibilityFade(
        visible = visible,
        modifier = modifier
    ) {
        content()
    }
}
