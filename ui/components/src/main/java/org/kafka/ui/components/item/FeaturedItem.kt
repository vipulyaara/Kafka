package org.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.common.image.Icons
import org.kafka.ui.components.placeholder.placeholderDefault
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalThemeColor

@Composable
fun FeaturedItem(
    item: Item,
    modifier: Modifier = Modifier,
    label: String? = null,
    imageUrl: String? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = shape
    ) {
        Box {
            CoverImage(
                data = imageUrl ?: item.coverImage,
                placeholder = if (item.isAudio) Icons.Audio else Icons.Texts,
                shape = shape,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            if (!label.isNullOrBlank()) {
                TextOverlay(label = label, item = item, shape = shape)
            }
        }
    }
}

@Composable
private fun BoxScope.TextOverlay(label: String, item: Item, shape: Shape) {
    val scrim = scrim(
        if (LocalThemeColor.current.isDark) {
            MaterialTheme.colorScheme.background
        } else {
            Color.Black
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomStart)
            .clip(shape)
            .background(scrim)
            .padding(Dimens.Gutter)
    ) {
        Text(
            text = label,
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

private fun scrim(color: Color) = Brush.verticalGradient(
    listOf(Color.Transparent, color.copy(alpha = 0.6f), color)
)
