package com.kafka.item.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.common.testTagUi
import com.kafka.common.widgets.IconButton
import com.kafka.common.widgets.IconResource
import com.kafka.common.widgets.shadowMaterial
import com.kafka.ui.components.material.FloatingButton
import com.sarahang.playback.ui.components.WIDE_LAYOUT_MIN_WIDTH
import kafka.ui.item.detail.generated.resources.Res
import kafka.ui.item.detail.generated.resources.cd_add_to_favorites
import kafka.ui.item.detail.generated.resources.cd_favorite
import kafka.ui.item.detail.generated.resources.cd_files
import kafka.ui.item.detail.generated.resources.cd_remove_from_favorites
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun ItemDetailActionsRow(
    ctaText: String,
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit,
    isFavorite: Boolean,
    favoriteLoading: Boolean,
    toggleFavorite: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            Modifier
                .widthIn(max = WIDE_LAYOUT_MIN_WIDTH)
                .align(Alignment.Center)
                .padding(horizontal = Dimens.Gutter, vertical = Dimens.Spacing12),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Gutter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.2f)) {
                FavoriteIcon(
                    isFavorite = isFavorite,
                    favoriteLoading = favoriteLoading,
                    modifier = Modifier.align(Alignment.Center),
                    onClicked = toggleFavorite
                )
            }

            FloatingButton(
                text = ctaText,
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth(),
                onClickLabel = ctaText,
                onClicked = onPrimaryAction
            )
        }
    }
}

@Composable
private fun FavoriteIcon(
    isFavorite: Boolean,
    favoriteLoading: Boolean,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    val background by animateColorAsState(if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
    val iconTint by animateColorAsState(if (isFavorite) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
    val icon = if (isFavorite) Icons.HeartFilled else Icons.Heart
    val contentDescription =
        if (isFavorite) Res.string.cd_remove_from_favorites else Res.string.cd_add_to_favorites

    Box(
        modifier = modifier
            .size(50.dp)
            .shadowMaterial(Dimens.Spacing12, CircleShape)
            .background(background)
            .clickable { onClicked() }
    ) {
        IconButton(
            onClick = onClicked,
            onClickLabel = stringResource(contentDescription),
            modifier = Modifier
                .align(Alignment.Center)
                .testTagUi(if (isFavorite) "remove_favorite" else "add_favorite")
        ) {
            IconResource(
                imageVector = icon,
                tint = iconTint,
                contentDescription = stringResource(Res.string.cd_favorite)
            )
        }
    }
}

@Composable
fun ShareIcon(shareEnabled: Boolean, modifier: Modifier = Modifier, shareItem: () -> Unit) {
    if (shareEnabled) {
        Box(modifier = modifier) {
            Icon(
                icon = Icons.Share,
                contentDescription = stringResource(Res.string.cd_files),
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTagUi("share"),
                onClicked = { shareItem() }
            )
        }
    }
}

@Composable
private fun Icon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .shadowMaterial(Dimens.Spacing12, CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClicked() }
    ) {
        IconButton(onClick = onClicked, modifier = Modifier.align(Alignment.Center)) {
            IconResource(
                imageVector = icon,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = contentDescription
            )
        }
    }
}
