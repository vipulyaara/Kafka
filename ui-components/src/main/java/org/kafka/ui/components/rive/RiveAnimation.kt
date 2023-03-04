package org.kafka.ui.components.rive

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.Loop

@Composable
fun RiveAnimation(
    @RawRes resource: Int,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false,
    loop: Loop = Loop.ONESHOT,
    update: (RiveAnimationView) -> Unit
) {
    AndroidView(modifier = modifier, factory = { context ->
        RiveAnimationView(context).apply {
            setRiveResource(resource, autoplay = autoPlay, loop = loop)
        }
    }) { view ->
        update(view)
    }
}
