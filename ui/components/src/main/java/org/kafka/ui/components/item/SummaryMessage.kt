package org.kafka.ui.components.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import org.kafka.common.widgets.shadowMaterial
import org.kafka.ui.components.R
import org.kafka.ui.components.animation.colorFilterDynamicProperty
import ui.common.theme.theme.Dimens

@Composable
fun SummaryMessage(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadowMaterial(
                elevation = Dimens.Spacing12,
                shape = RoundedCornerShape(Dimens.Spacing04)
            ),
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(Dimens.Radius04),
        color = MaterialTheme.colorScheme.surface,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.Spacing16, vertical = Dimens.Spacing06),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing08),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummaryAnimation(
                modifier = Modifier.size(Dimens.Spacing40),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.weight(1f)
            )

            BetaLabel()
        }
    }
}

@Composable
private fun SummaryAnimation(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
) {
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

@Composable
private fun BetaLabel() {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(Dimens.Radius12)
    ) {
        Text(
            text = stringResource(R.string.beta),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = Dimens.Spacing08, vertical = Dimens.Spacing04)
        )
    }
}
