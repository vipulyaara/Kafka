package com.kafka.ui.components.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.common.extensions.medium
import com.kafka.common.extensions.rememberSavableMutableState
import com.kafka.common.image.Icons
import com.kafka.common.simpleClickable
import com.kafka.data.entities.Reaction
import com.kafka.data.entities.Review
import ui.common.theme.theme.Dimens

@Composable
fun ReviewItem(
    review: Review,
    modifier: Modifier = Modifier,
    maxLines: Int = 4,
    react: (Reaction) -> Unit = {}
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Dimens.Spacing08),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
        ) {
            UserHeader(name = review.userId, rating = review.rating, image = null)
            ReviewText(reviewId = review.reviewId, text = review.text, maxLines = maxLines)
            Reactions(likes = review.likes, dislikes = review.dislikes, updateReaction = react)
        }
    }
}

@Composable
private fun ReviewText(reviewId: String, text: String, maxLines: Int) {
    var isExpanded by rememberSavableMutableState(reviewId) { false }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .animateContentSize()
            .simpleClickable { isExpanded = !isExpanded }
    )
}

@Composable
private fun Reactions(
    likes: Int,
    dislikes: Int,
    modifier: Modifier = Modifier,
    updateReaction: (Reaction) -> Unit
) {
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
private fun UserHeader(
    name: String?,
    image: String?,
    rating: Float,
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
            Text(
                text = name.orEmpty(),
                style = MaterialTheme.typography.titleSmall.medium(),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

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
