package com.kafka.kms.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    onImeAction: (String) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .border(
                width = 1.2.dp,
                color = if (value.isEmpty()) MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Enter) {
                    onImeAction(value)
                    true
                } else {
                    false
                }
            },
        color = if (value.isEmpty())
            MaterialTheme.colorScheme.surface
        else
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            minLines = minLines,
            maxLines = maxLines,
            readOnly = readOnly,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun LongTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minLines: Int = 3,
    onImeAction: (String) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .border(
                width = 1.2.dp,
                color = if (value.isEmpty()) MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Enter) {
                    onImeAction(value)
                    true
                } else {
                    false
                }
            },
        color = if (value.isEmpty())
            MaterialTheme.colorScheme.surface
        else
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            minLines = minLines,
            maxLines = Int.MAX_VALUE,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                innerTextField()
            }
        )
    }
} 