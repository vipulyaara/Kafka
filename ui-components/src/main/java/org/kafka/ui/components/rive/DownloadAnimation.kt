package org.kafka.ui.components.rive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import org.kafka.ui.components.R

@Composable
fun DownloadAnimation(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.download))
    Box(
        modifier = modifier
            .size(200.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        LottieAnimation(
            composition = composition,
//            dynamicProperties = colorFilterDynamicProperty(color),
//            iterations = LottieConstants.IterateForever,
            modifier = Modifier.align(Alignment.Center),
//            isPlaying = true,
            clipSpec = LottieClipSpec.Progress(progress),
        )
    }
}

@Composable
fun colorFilterDynamicProperty(color: Color = MaterialTheme.colorScheme.secondary) =
    rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(color.toArgb()),
            keyPath = arrayOf(
                "**",
            )
        ),
    )

private const val musicBars = "https://assets1.lottiefiles.com/packages/lf20_NsCkXA/music.json"
