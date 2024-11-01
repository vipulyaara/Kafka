package com.kafka.common.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.extensions.alignCenter
import com.kafka.common.snackbar.UiMessage
import com.kafka.common.snackbar.asString
import kafka.ui.common.generated.resources.Res
import kafka.ui.common.generated.resources.absurd_meditation
import org.jetbrains.compose.resources.painterResource
import ui.common.theme.theme.Dimens

@Composable
fun FullScreenMessage(
    uiMessage: UiMessage?,
    modifier: Modifier = Modifier,
    show: Boolean = uiMessage != null,
    onRetry: (() -> Unit)? = null
) {
    AnimatedVisibilityFade(visible = show, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 48.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(Res.drawable.absurd_meditation),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .weight(0.6f)
                    .padding(48.dp)
            )

            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiMessage?.asString().orEmpty(),
                    style = MaterialTheme.typography.titleMedium.alignCenter(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                )

                onRetry?.let {
                    Spacer(modifier = Modifier.height(Dimens.Spacing08))

                    Text(
                        text = "Please try again",
                        style = MaterialTheme.typography.titleMedium.alignCenter(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    )

                    Spacer(modifier = Modifier.height(Dimens.Spacing24))

                    Button(onClick = onRetry) {
                        Text(
                            text = "Retry",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }
}
