package com.kafka.ui.components.item.review

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.style.TextOverflow
import com.kafka.common.extensions.medium
import com.kafka.common.extensions.rememberSavableMutableState
import com.kafka.common.image.Icons
import com.kafka.common.simpleClickable
import com.kafka.data.entities.Review
import com.kafka.ui.components.item.PersonAvatar
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.common.theme.theme.Dimens

@Composable
fun ReviewItem(
    review: Review,
    modifier: Modifier = Modifier,
    maxLines: Int = 4,
    reactions: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Dimens.Spacing08),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
        ) {
            UserHeader(
                name = review.userName,
                rating = review.rating,
                image = null,
                createdAt = review.createdAt
            )
            ReviewText(reviewId = review.reviewId, text = review.text, maxLines = maxLines)

            reactions()
        }
    }
}

@Composable
private fun ReviewText(reviewId: String, text: String, maxLines: Int) {
    val textState = rememberRichTextState().apply { setMarkdown(text) }
    var isExpanded by rememberSavableMutableState(reviewId) { false }

    SelectionContainer {
        RichText(
            state = textState,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .animateContentSize()
                .simpleClickable { isExpanded = !isExpanded }
        )
    }
}

@Composable
private fun UserHeader(
    name: String?,
    image: String?,
    rating: Float,
    createdAt: Instant,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PersonAvatar(
            imageUrl = image.orEmpty(),
            title = name,
            modifier = Modifier.size(Dimens.Spacing48)
        )

        Column(verticalArrangement = Arrangement.spacedBy(Dimens.Spacing06)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name.orEmpty(),
                    style = MaterialTheme.typography.titleSmall.medium(),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "•",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = formatDate(createdAt),
                    style = MaterialTheme.typography.bodySmall.medium(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Rating(rating = rating)
        }
    }
}

@Composable
fun Rating(rating: Float) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing02),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(MaxRating) { index ->
            val starProgress = rating - index
            when {
                // Full star
                starProgress >= 1 -> Icon(
                    imageVector = Icons.Star,
                    modifier = Modifier.size(Dimens.Spacing16),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )

                // Half star
                starProgress > 0 -> Box(
                    modifier = Modifier.size(Dimens.Spacing16),
                    contentAlignment = Alignment.Center
                ) {
                    // Empty star background
                    Icon(
                        imageVector = Icons.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant
                    )
                    // Filled star clipped to half
                    Icon(
                        imageVector = Icons.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clipToBounds()
                            .drawWithContent {
                                clipRect(right = size.width / 2f) {
                                    this@drawWithContent.drawContent()
                                }
                            }
                    )
                }

                // Empty star
                else -> Icon(
                    imageVector = Icons.Star,
                    modifier = Modifier.size(Dimens.Spacing16),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

private const val MaxRating = 5

// example formatted date: 6 Dec 2024
private fun formatDate(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val month = localDateTime.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "${localDateTime.dayOfMonth} $month ${localDateTime.year}"
}
