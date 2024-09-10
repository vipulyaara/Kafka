package org.kafka.ui.components.material

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
fun AlertDialog(
    title: String,
    modifier: Modifier = Modifier,
    text: String? = null,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    cancelButton: @Composable (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
) {
    androidx.compose.material3.AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = properties,
        title = { Text(text = title, style = MaterialTheme.typography.bodyMedium) },
        text = text?.let { { Text(text = it, style = MaterialTheme.typography.bodySmall) } },
        confirmButton = confirmButton,
        dismissButton = {
            if (cancelButton != null) {
                cancelButton()
            }
        }
    )
}

@Composable
fun AlertDialogAction(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(modifier = modifier, onClick = onClick) {
        Text(text = text, style = MaterialTheme.typography.titleSmall)
    }
}
