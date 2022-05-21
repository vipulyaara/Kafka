package org.kafka.common.widgets

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import org.kafka.common.R
import org.kafka.common.UiError
import org.kafka.common.UiMessage
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.extensions.alignCenter
import ui.common.theme.theme.body2

@Composable
fun ErrorMessage(
    message: UiMessage?,
    modifier: Modifier = Modifier,
    show: Boolean = message != null
) {
    ErrorMessage(
        snackbarError = message?.message,
        show = show,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun FullScreenMessage(
    message: UiMessage?,
    show: Boolean = message != null,
    onRetry: (() -> Unit)? = null
) {
    FullScreenError(uiError = message?.let { UiError(it.message) }, show = show, onRetry = onRetry)
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
    uiError: UiError?,
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

@Composable
fun PaginatedError(
    modifier: Modifier = Modifier,
    show: Boolean = true,
    message: String,
    onRetry: () -> Unit = {}
) {
    AnimatedVisibility(visible = show, enter = fadeIn(), exit = fadeOut()) {
        Surface(modifier = modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = modifier
                    .clickable(onClick = onRetry)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "\n\nClick to retry",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
