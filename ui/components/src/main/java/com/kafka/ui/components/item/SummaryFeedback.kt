package com.kafka.ui.components.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.Dimens

@Composable
fun SummaryFeedback(modifier: Modifier = Modifier) {
    var feedbackProvided by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Radius08),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier
                .padding(Dimens.Gutter)
                .animateContentSize()
        ) {
            if (!feedbackProvided) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Gutter)) {
                    FeedbackButton(feedback = FeedbackItem.Positive) { feedbackProvided = true }
                    FeedbackButton(feedback = FeedbackItem.Negative) { feedbackProvided = true }
                }
            }

            if (feedbackProvided) {
                Text(
                    text = "Thank you for your feedback!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(Dimens.Spacing08)
                )
            }
        }
    }
}

@Composable
private fun FeedbackButton(feedback: FeedbackItem, onSelected: (FeedbackItem) -> Unit) {
    Surface(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(Dimens.Radius04),
        onClick = { onSelected(feedback) }
    ) {
        Text(
            text = feedback.icon + "  " + feedback.label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = Dimens.Spacing08, horizontal = Dimens.Gutter)
        )
    }
}

enum class FeedbackItem(val icon: String, val label: String) {
    Positive("\uD83D\uDE0D", "Liked it"),
    Negative("\uD83D\uDE14", "Not helpful"),
}

@Composable
@Preview
private fun SummaryFeedbackPreview() {
    SummaryFeedback()
}
