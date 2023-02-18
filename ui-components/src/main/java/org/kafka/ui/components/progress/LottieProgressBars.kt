package org.kafka.ui.components.progress

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import org.kafka.ui.components.R

@Composable
fun LottieProgressBar(modifier: Modifier = Modifier, show: Boolean = true) {
    if (show) {
        val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.open_book))

        val dynamicProperties = rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR,
                value = MaterialTheme.colorScheme.primary.toArgb(),
                keyPath = arrayOf(
                    "Composição 1",
                    "página 2",
                    "Caminho 1",
                )
            ),
        )

        LottieAnimation(
            composition = composition1,
            modifier = modifier.size(32.dp),
            iterations = LottieConstants.IterateForever,
            dynamicProperties = dynamicProperties,
        )
    }
}
