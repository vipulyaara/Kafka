package org.kafka.common.widgets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.common.theme.theme.Dimens

@Composable
fun KafkaSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { Snackbar(data = it, onDismiss = onDismiss) },
        modifier = modifier
    )
}

@Composable
fun Snackbar(
    data: SnackbarData,
    onDismiss: () -> Unit,
    snackbar: @Composable (SnackbarData) -> Unit = {
        Snackbar(
            snackbarData = it,
            shape = RoundedCornerShape(Dimens.Spacing04),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    }
) {
    Dismissable(onDismiss = onDismiss) {
        snackbar(data)
    }
}

@Composable
fun Dismissable(
    onDismiss: () -> Unit,
    directions: Set<DismissDirection> = setOf(
        DismissDirection.StartToEnd,
        DismissDirection.EndToStart
    ),
    content: @Composable () -> Unit
) {
    val dismissState = rememberDismissState(confirmValueChange = {
        if (it != DismissValue.Default) {
            onDismiss.invoke()
        }
        true
    })
    SwipeToDismiss(
        state = dismissState,
        directions = directions,
        background = {},
        dismissContent = { content() }
    )
}
