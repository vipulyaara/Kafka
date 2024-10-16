package com.kafka.ui.components.item

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kafka.ui.components.R
import com.kafka.ui.components.animation.colorFilterDynamicProperty

@Composable
actual fun SummaryAnimation(modifier: Modifier, color: Color) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.book))

    Box(modifier = modifier) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = modifier.align(Alignment.Center),
            isPlaying = true,
            speed = 0.5f,
            dynamicProperties = colorFilterDynamicProperty(color)
        )
    }
}
