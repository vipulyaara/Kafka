package com.kafka.reader.epub.context

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.Dimens

@Composable
fun ContextActions(
    text: String,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    onHighlight: () -> Unit,
    onCopy: () -> Unit,
    onTranslate: () -> Unit,
    onAiExplain: () -> Unit,
    dismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    AppTheme(isDarkTheme = !isDarkTheme) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                        .padding(4.dp)
                        .border(2.dp, MaterialTheme.colorScheme.error, CircleShape),
                )

                ContextActionButton(
                    icon = Icons.Copy,
                    contentDescription = "Copy",
                    onClick = {
                        clipboardManager.setText(AnnotatedString(text))
                        dismiss()
                    }
                )

                ContextActionButton(
                    icon = Icons.Translate,
                    contentDescription = "Translate",
                    onClick = onTranslate
                )

                ContextActionButton(
                    icon = Icons.Assistant,
                    contentDescription = "AI Explain",
                    onClick = onAiExplain
                )
            }
        }
    }
}

@Composable
private fun ContextActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        imageVector = icon,
        contentDescription = contentDescription,
        onClick = onClick,
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.size(32.dp)
    )
}
