package com.kafka.ui.components.item

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.common.extensions.medium
import com.kafka.common.image.Icons
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.surfaceDeep

@Composable
fun ReviewItem(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceDeep,
        shape = RoundedCornerShape(Dimens.Radius08)
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
        ) {
            UserHeader(name = "Vipul Kumar", image = null)
            ReviewText(text = reviewText)
        }
    }
}

@Composable
private fun ReviewText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun UserHeader(
    name: String?,
    image: String?,
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
            modifier = Modifier.size(44.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(Dimens.Spacing06)) {
            Text(
                text = name.orEmpty(),
                style = MaterialTheme.typography.titleSmall.medium(),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Rating(3f)
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
                starProgress > 0 -> Icon(
                    imageVector = Icons.StarHalf,
                    modifier = Modifier.size(Dimens.Spacing16),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
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
private const val reviewText = """Tolstoy's masterpiece weaves an intricate tapestry of 19th century Russian society through the parallel stories of Anna's tragic affair and Levin's search for meaning. 
The novel's psychological depth and insight into human nature remain unmatched, with each character drawn in vivid, complex detail.
While Anna's passionate rebellion against societal constraints leads to her downfall, Levin's philosophical journey offers a counterbalancing path to fulfillment.
The author's genius lies in making deeply personal struggles feel universal, while simultaneously critiquing the hypocrisies of aristocratic society.
Despite its intimidating length, the novel's themes of love, marriage, faith, and social convention remain startlingly relevant to modern readers.
"""