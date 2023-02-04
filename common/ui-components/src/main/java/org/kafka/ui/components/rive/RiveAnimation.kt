package org.kafka.ui.components.rive

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.Loop
import org.kafka.ui.components.R

@Composable
fun RiveAnimation(modifier: Modifier = Modifier, progress: Float) {
    AndroidView(modifier = modifier, factory = { context ->
        RiveAnimationView(context).apply {
            setRiveResource(R.raw.liquid_download_2, autoplay = false, loop = Loop.ONESHOT)
        }
    }) { view ->
        view.setBooleanState("State machine 1", "Downloading", true)
        view.setNumberState("State machine 1", "Progress", progress * 100)
    }
}
