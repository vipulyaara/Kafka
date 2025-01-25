package com.kafka.ui.components.editor

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.kafka.common.image.Icons
import com.mohamedrejeb.richeditor.model.RichTextState
import ui.common.theme.theme.Dimens

@Composable
fun EditorActions(
    textState: RichTextState,
    modifier: Modifier = Modifier,
    actions: List<EditorAction> = EditorAction.all
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Dimens.Gutter),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        actions.forEach { action ->
            val currentDecoration = textState.currentSpanStyle.textDecoration
            val isActive = when (action) {
                is EditorAction.Bold -> textState.currentSpanStyle.fontWeight == FontWeight.Bold
                is EditorAction.Italic -> textState.currentSpanStyle.fontStyle == FontStyle.Italic
                is EditorAction.Underline -> currentDecoration?.contains(TextDecoration.Underline) == true
                is EditorAction.StrikeThrough -> currentDecoration?.contains(TextDecoration.LineThrough) == true
                is EditorAction.BulletList -> textState.isUnorderedList
                is EditorAction.NumberList -> textState.isOrderedList
                else -> false
            }
            
            EditorActionButton(
                action = action,
                isActive = isActive,
                onClick = {
                    when (action) {
                        is EditorAction.Bold -> {
                            textState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        }
                        is EditorAction.Italic -> {
                            textState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        }
                        is EditorAction.Underline -> {
                            textState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                        }
                        is EditorAction.StrikeThrough -> {
                            textState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                        }
                        is EditorAction.BulletList -> {
                            textState.toggleUnorderedList()
                        }
                        is EditorAction.NumberList -> {
                            textState.toggleOrderedList()
                        }
                        else -> { /* Other actions to be implemented */ }
                    }
                }
            )
        }
    }
}

@Composable
private fun EditorActionButton(
    action: EditorAction,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surfaceColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    )
    val contentColor by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    )
    Surface(
        modifier = modifier.size(Dimens.Spacing40),
        shape = RoundedCornerShape(Dimens.Radius08),
        color = surfaceColor,
        border = BorderStroke(Dimens.Border01, MaterialTheme.colorScheme.surfaceContainerHigh),
        onClick = onClick
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = action.contentDescription,
            modifier = Modifier.padding(Dimens.Spacing08),
            tint = contentColor
        )
    }
}

sealed class EditorAction(
    val icon: ImageVector,
    val contentDescription: String
) {
    data object Bold : EditorAction(Icons.Bold, "Toggle Bold")
    data object Italic : EditorAction(Icons.Italic, "Toggle Italic")
    data object Underline : EditorAction(Icons.Underline, "Toggle Underline")
    data object StrikeThrough : EditorAction(Icons.StrikeThrough, "Toggle Strikethrough")
    data object BulletList : EditorAction(Icons.OrderedList, "Toggle Bullet List")
    data object NumberList : EditorAction(Icons.UnorderedList, "Toggle Number List")
    data object Indent : EditorAction(Icons.Underline, "Increase Indent")
    data object Outdent : EditorAction(Icons.Underline, "Decrease Indent")

    companion object {
        val all = listOf(
            Bold,
            Italic,
            Underline,
            StrikeThrough,
            BulletList,
            NumberList,
            Indent,
            Outdent
        )
    }
}
