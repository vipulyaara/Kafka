package org.kafka.item.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.ItemDetail
import com.sarahang.playback.ui.components.WIDE_LAYOUT_MIN_WIDTH
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.common.widgets.shadowMaterial
import org.kafka.item.R
import org.kafka.ui.components.material.FloatingButton
import ui.common.theme.theme.Dimens

@Composable
fun ItemDetailActions(
    itemDetail: ItemDetail,
    onPrimaryAction: (String) -> Unit,
    openFiles: (String) -> Unit,
    isFavorite: Boolean,
    toggleFavorite: () -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .widthIn(max = WIDE_LAYOUT_MIN_WIDTH)
                .align(Alignment.Center)
                .padding(horizontal = 24.dp, vertical = Dimens.Spacing12),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.2f)) {
                FavoriteIcon(
                    isFavorite = isFavorite,
                    modifier = Modifier.align(Alignment.Center)
                ) { toggleFavorite() }
            }

            Box(modifier = Modifier.weight(0.2f)) {
                Icon(
                    icon = Icons.Download,
                    modifier = Modifier.align(Alignment.Center),
                    onClicked = { openFiles(itemDetail.itemId) }
                )
            }

            FloatingButton(
                text = stringResource(if (itemDetail.isAudio) R.string.play else R.string.read),
                modifier = Modifier.weight(0.8f),
                onClicked = { onPrimaryAction(itemDetail.itemId) }
            )
        }
    }
}

@Composable
private fun FavoriteIcon(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    val background by animateColorAsState(if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
    val iconTint by animateColorAsState(if (isFavorite) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
    val icon = if (isFavorite) Icons.HeartFilled else Icons.Heart

    Box(
        modifier = modifier
            .size(50.dp)
            .shadowMaterial(Dimens.Spacing12, CircleShape)
            .background(background)
            .clickable { onClicked() }
    ) {
        IconButton(onClick = onClicked, modifier = Modifier.align(Alignment.Center)) {
            IconResource(imageVector = icon, tint = iconTint)
        }
    }
}

@Composable
private fun Icon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .shadowMaterial(Dimens.Spacing12, CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClicked() }
    ) {
        IconButton(onClick = onClicked, modifier = Modifier.align(Alignment.Center)) {
            IconResource(imageVector = icon)
        }
    }
}
