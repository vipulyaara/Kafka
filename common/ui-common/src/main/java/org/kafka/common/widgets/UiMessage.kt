package org.kafka.common.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.contentColorFor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import org.kafka.common.R
import org.kafka.common.UiMessage
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.extensions.alignCenter
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.body2

@Composable
fun FullScreenMessage(
    uiMessage: UiMessage?,
    show: Boolean = uiMessage != null,
    onRetry: (() -> Unit)? = null
) {
    AnimatedVisibility(visible = show) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LoadImage(
                data = R.drawable.img_absurd_error,
                modifier = Modifier
                    .aspectRatio(1f)
                    .weight(0.6f)
                    .padding(48.dp),
                tint = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
            )

            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiMessage?.title.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(Dimens.Spacing08))

                Text(
                    text = uiMessage?.message.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium.alignCenter(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.4f)
                )

                onRetry?.let {
                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(onClick = onRetry) {
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

@Composable
fun InlineMessage(
    snackbarError: String?,
    modifier: Modifier = Modifier,
    show: Boolean = snackbarError != null,
) {
    if (show) {
        Card(
            modifier = modifier,
            elevation = 1.dp,
            shape = RectangleShape,
            backgroundColor = MaterialTheme.colorScheme.error
        ) {
            Text(
                modifier = Modifier.padding(Dimens.Spacing12),
                text = snackbarError.orEmpty(),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
