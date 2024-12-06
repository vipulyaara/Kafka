package com.kafka.ui.components.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.style.TextOverflow
import com.kafka.common.extensions.medium
import com.kafka.common.image.Icons
import com.kafka.data.entities.Review
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.surfaceDeep

@Composable
fun ReviewItem(review: Review, modifier: Modifier = Modifier, maxLines: Int = 4) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceDeep,
        shape = RoundedCornerShape(Dimens.Radius08)
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
        ) {
            UserHeader(name = review.userId, rating = review.rating, image = null)
            ReviewText(text = review.text, maxLines = maxLines)
        }
    }
}

@Composable
private fun ReviewText(text: String, maxLines: Int = 4) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
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
