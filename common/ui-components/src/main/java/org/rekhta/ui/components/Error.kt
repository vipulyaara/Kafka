package org.rekhta.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import org.kafka.common.animation.Delayed
import org.kafka.common.extensions.alignCenter
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.body2

@Composable
fun DelayedErrorBox(
    modifier: Modifier = Modifier,
    title: String = "",
    message: String = stringResource(R.string.error_unknown),
    retryVisible: Boolean = true,
    onRetryClick: () -> Unit = {}
) {
    Delayed {
        ErrorBox(modifier, title, message, retryVisible, onRetryClick)
    }
}

@Preview
@Composable
fun ErrorBox(
    modifier: Modifier = Modifier,
    title: String = "Error title",
    message: String = stringResource(R.string.error_unknown),
    retryVisible: Boolean = true,
    onRetryClick: () -> Unit = {}
) {
    val wavesComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sleepy_cat))

    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingTiny),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        LottieAnimation(
            wavesComposition,
            Modifier.size(400.dp),
            iterations = LottieConstants.IterateForever
        )

        Text(title, style = MaterialTheme.typography.titleMedium)

        Text(message, style = MaterialTheme.typography.body2.alignCenter())

        if (retryVisible)
            TextButton(
                onClick = onRetryClick,
                modifier = Modifier.padding(top = AppTheme.specs.padding)
            ) {
                Text(text = "Retry")
            }
    }
}
