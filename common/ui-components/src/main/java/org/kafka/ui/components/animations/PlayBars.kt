package org.kafka.ui.components.animations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty

@Composable
fun PlayBars(size: Dp) {
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onSurface.toArgb(),
            keyPath = intArrayOf(5).mapIndexed { index, i -> "bar $index" }.toTypedArray()
        ),
    )
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url(musicBars))

    Box(modifier = Modifier.size(size)) {
        LottieAnimation(
            composition = composition,
            dynamicProperties = dynamicProperties,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private const val musicBars = "https://assets8.lottiefiles.com/private_files/lf30_fah4ouxp.json"
