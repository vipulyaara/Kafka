package org.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.kafka.data.entities.Item
import org.kafka.common.image.Icons
import org.kafka.ui.components.placeholder.placeholderDefault
import ui.common.theme.theme.Dimens

@Composable
fun FeaturedItem(
    item: Item,
    modifier: Modifier = Modifier,
    label: String? = null,
    imageUrl: String? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(Dimens.RadiusMedium)
    ) {
        Box {
            CoverImage(
                data = imageUrl ?: item.coverImage,
                placeholder = if (item.isAudio) Icons.Audio else Icons.Texts,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Scrim)
                    .padding(Dimens.Gutter)
            ) {
                Text(
                    text = label ?: item.title.orEmpty(),
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing08)
                ) {
                    item.creator?.let { creator ->
                        Text(
                            text = creator.name,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White.copy(alpha = 0.6f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedItemPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(horizontal = Dimens.Gutter)
            .padding(top = Dimens.Gutter, bottom = Dimens.Spacing12)
            .clip(RoundedCornerShape(Dimens.RadiusMedium))
            .placeholderDefault()
    )
}

private val Scrim = Brush.verticalGradient(
    listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f), Color.Black)
)
