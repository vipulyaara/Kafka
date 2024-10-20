package com.kafka.user.home.overlays

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.kafka.common.extensions.alignCenter
import com.kafka.common.extensions.semiBold
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.data.model.AppMessage
import com.kafka.ui.components.material.TextButton
import ui.common.theme.theme.Dimens

@Composable
fun AppMessage(
    appMessage: AppMessage?,
    snackbarManager: SnackbarManager,
    onPrimaryClick: (AppMessage) -> Unit,
    dismiss: () -> Unit
) {
    if (appMessage != null) {
        if (appMessage.type == AppMessage.Type.Hard) {
            Dialog(
                onDismissRequest = dismiss,
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                DialogContent(
                    appMessage = appMessage,
                    modifier = Modifier.padding(Dimens.Spacing24),
                    onPrimaryClick = { onPrimaryClick(appMessage) },
                    dismiss = dismiss
                )
            }
        }
    }

    LaunchedEffect(appMessage) {
        if (appMessage != null && appMessage.type == AppMessage.Type.Soft) {
            snackbarManager.addMessage(
                message = appMessage.snackbarMessage,
                label = appMessage.primaryAction.ifEmpty { appMessage.secondaryAction },
                onClick = { onPrimaryClick(appMessage) }
            )
        }
    }
}

@Composable
private fun DialogContent(
    appMessage: AppMessage,
    modifier: Modifier = Modifier,
    onPrimaryClick: () -> Unit,
    dismiss: () -> Unit
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(Dimens.Radius12)) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = Dimens.Spacing32, horizontal = Dimens.Spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (appMessage.title.isNotEmpty()) {
                Text(
                    text = appMessage.title,
                    modifier = Modifier.padding(horizontal = Dimens.Spacing24),
                    style = MaterialTheme.typography.titleLarge.semiBold().alignCenter(),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (appMessage.text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.Spacing08))
                Text(
                    text = appMessage.text,
                    modifier = Modifier.padding(horizontal = Dimens.Spacing24),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (appMessage.image.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.Spacing24))
                AsyncImage(
                    model = appMessage.image,
                    contentDescription = null,
                    modifier = Modifier.heightIn(max = 200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Spacing08))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = Dimens.Spacing24)
            ) {
                if (appMessage.secondaryAction.isNotEmpty()) {
                    TextButton(
                        text = appMessage.secondaryAction,
                        color = MaterialTheme.colorScheme.tertiary,
                        onClick = dismiss
                    )
                }

                if (appMessage.primaryAction.isNotEmpty()) {
                    TextButton(text = appMessage.primaryAction, onClick = onPrimaryClick)
                }
            }
        }
    }
}
