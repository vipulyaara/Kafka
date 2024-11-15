@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.kafka.ui.components.item

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.data.entities.Item
import com.kafka.ui.components.placeholder.placeholderDefault
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalThemeColor

@Composable
fun FeaturedItem(
    item: Item,
    modifier: Modifier = Modifier,
    label: String? = null,
    imageUrl: String? = null,
    shape: Shape = RoundedCornerShape(Dimens.Radius16),
    aspectRatio: Float = 1f,
    onClick: () -> Unit = {},
) {
    FeaturedItem(
        placeHolder = if (item.isAudio) Icons.Audio else Icons.Texts,
        coverImage = item.coverImage,
        aspectRatio = aspectRatio,
        creator = item.creator,
        modifier = modifier,
        label = label,
        imageUrl = imageUrl,
        shape = shape,
        onClick = onClick,
    )
}

@Composable
fun FeaturedItem(
    coverImage: String?,
    modifier: Modifier = Modifier,
    creator: String? = null,
    label: String? = null,
    imageUrl: String? = null,
    placeHolder: ImageVector = CoverDefaults.placeholder,
    shape: Shape = RoundedCornerShape(16.dp),
    aspectRatio: Float = 1f,
    onClick: () -> Unit = {},
) {
    Surface(modifier = modifier.fillMaxWidth(), onClick = onClick, shape = shape) {
        Box {
            CoverImage(
                data = imageUrl ?: coverImage,
                placeholder = placeHolder,
                shape = shape,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
            )

            if (!label.isNullOrBlank()) {
                TextOverlay(label = label, creator = creator, shape = shape)
            }
        }
    }
}

@Composable
private fun BoxScope.TextOverlay(label: String, creator: String?, shape: Shape) {
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
            .padding(Dimens.Spacing24)
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
            creator?.let { creator ->
                Text(
                    text = creator,
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
