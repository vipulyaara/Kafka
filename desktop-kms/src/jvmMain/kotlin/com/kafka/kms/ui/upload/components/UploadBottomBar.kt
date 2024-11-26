package com.kafka.kms.ui.upload.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import java.awt.Cursor

@Composable
fun UploadBottomBar(
    isEnabled: Boolean,
    onUploadClick: () -> Unit,
    uploadStatus: String,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status messages
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uploadStatus.isNotEmpty()) {
                    Text(
                        uploadStatus,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .hoverable(remember { MutableInteractionSource() })
                            .pointerHoverIcon(PointerIcon(Cursor(Cursor.TEXT_CURSOR)))
                    )
                }
                if (errorMessage.isNotEmpty()) {
                    SelectionContainer {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .hoverable(remember { MutableInteractionSource() })
                                .pointerHoverIcon(PointerIcon(Cursor(Cursor.TEXT_CURSOR)))
                        )
                    }
                }
            }

            // Upload button
            Surface(
                modifier = Modifier
                    .widthIn(min = 160.dp)
                    .clip(MaterialTheme.shapes.small)
                    .border(
                        width = 1.5.dp,
                        color = if (!isEnabled) MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable(
                        enabled = isEnabled,
                        onClick = onUploadClick
                    ),
                color = if (!isEnabled) 
                    MaterialTheme.colorScheme.surface
                else 
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Upload,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (!isEnabled)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Upload Book",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (!isEnabled)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
} 