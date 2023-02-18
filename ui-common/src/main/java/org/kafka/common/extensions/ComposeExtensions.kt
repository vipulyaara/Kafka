package org.kafka.common.extensions

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

fun TextStyle.alignCenter() = merge(TextStyle(textAlign = TextAlign.Center))

@Composable
inline fun <T> rememberMutableState(
    key: Any? = null,
    crossinline init: @DisallowComposableCalls () -> T
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
    content: @Composable AnimatedVisibilityScope.() -> Unit
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
fun <T> collectEvent(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: (T) -> Unit
): Unit = LaunchedEffect(lifecycle, flow) {
    lifecycle.repeatOnLifecycle(minActiveState) {
        flow.collect {
            collector(it)
        }
    }
}
