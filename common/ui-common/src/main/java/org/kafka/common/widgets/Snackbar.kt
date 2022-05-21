package org.kafka.common.widgets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RekhtaSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    androidx.compose.material3.SnackbarHost(
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
        androidx.compose.material3.Snackbar(
            it,
            shape = RoundedCornerShape(4.dp),
            containerColor = MaterialTheme.colorScheme.primary
        )
    }
) {
    Dismissable(onDismiss = onDismiss) {
        snackbar(data)
    }
}
