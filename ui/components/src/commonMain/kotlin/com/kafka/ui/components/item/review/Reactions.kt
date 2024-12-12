package com.kafka.ui.components.item.review

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.data.entities.Reaction
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.surfaceDeep

@Composable
fun Reactions(
    likes: Int,
    dislikes: Int,
    modifier: Modifier = Modifier,
    updateReaction: (Reaction) -> Unit,
    edit: () -> Unit,
    delete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OverflowActions(
        expanded = expanded,
        onDismiss = { expanded = false },
        edit = edit,
        delete = delete
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Reaction(
            reaction = Reaction.Like,
            count = likes,
            tint = MaterialTheme.colorScheme.primary,
            onClick = { updateReaction(Reaction.Like) }
        )

        Reaction(
            reaction = Reaction.Dislike,
            count = dislikes,
            tint = MaterialTheme.colorScheme.error,
            onClick = { updateReaction(Reaction.Dislike) }
        )

        Spacer(Modifier.weight(1f))

        MoreOptions { expanded = true }
    }
}

@Composable
private fun Reaction(
    reaction: Reaction,
    count: Int,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Radius08),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.Spacing16, vertical = Dimens.Spacing04),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (reaction == Reaction.Like) "\uD83D\uDC4D" else "\uD83D\uDC4E",
                style = MaterialTheme.typography.bodyMedium,
                color = tint,
            )

            if (count > 0) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = tint,
                )
            }
        }
    }
}

@Composable
private fun MoreOptions(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Radius08),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.OverflowMenuHorizontal,
            modifier = Modifier.padding(horizontal = Dimens.Spacing08, vertical = Dimens.Spacing04),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "more options"
        )
    }
}

@Composable
private fun OverflowActions(
    expanded: Boolean,
    onDismiss: () -> Unit,
    edit: () -> Unit,
    delete: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.surfaceDeep),
    ) {
        ReviewActions.entries.forEach { item ->
            OverflowItem(text = item.text, onDismiss = onDismiss, onClick = {
                when (item) {
                    ReviewActions.Delete -> delete()
                }
            })
        }
    }
}

@Composable
private fun OverflowItem(text: String, onDismiss: () -> Unit, onClick: () -> Unit) {
    DropdownMenuItem(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceDeep),
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        onClick = {
            onDismiss()
            onClick()
        }
    )
}

enum class ReviewActions(val text: String) { Delete("Delete") }
