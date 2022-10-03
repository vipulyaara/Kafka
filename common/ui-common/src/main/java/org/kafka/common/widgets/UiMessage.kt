package org.kafka.common.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import org.kafka.common.R
import org.kafka.common.UiMessage
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.extensions.alignCenter
import ui.common.theme.theme.body2

@Composable
fun FullScreenMessage(
    message: UiMessage?,
    show: Boolean = message != null,
    onRetry: (() -> Unit)? = null
) {
    FullScreenError(
        uiError = message?.let { UiMessage(it.message) },
        show = show,
        onRetry = onRetry
    )
}

@Composable
fun ErrorMessage(
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
                modifier = Modifier.padding(12.dp),
                text = snackbarError.orEmpty(),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun FullScreenError(
    uiError: UiMessage?,
    show: Boolean = uiError != null,
    onRetry: (() -> Unit)? = null
) {
    AnimatedVisibility(visible = show) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
                .padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(0.1f))
            LoadImage(
                data = R.drawable.img_absurd_error,
                backgroundColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .weight(0.4f)
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.titleLarge.alignCenter()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = uiError?.message.orEmpty(),
                style = MaterialTheme.typography.bodyMedium.alignCenter()
            )
            Spacer(modifier = Modifier.height(12.dp))

            onRetry?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Retry",
                    style = MaterialTheme.typography.titleMedium.alignCenter(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onRetry() }
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}
