package org.kafka.ui.components.material

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AlertDialog(
    title: String,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable RowScope.() -> Unit,
    cancelButton: @Composable (RowScope.() -> Unit)? = null,
) {
    androidx.compose.material3.AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        },
        confirmButton = {
            Row {
                confirmButton()
                if (cancelButton != null) {
                    cancelButton()
                }
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
