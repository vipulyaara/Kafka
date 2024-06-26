package org.kafka.item.detail

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarahang.playback.ui.components.WIDE_LAYOUT_MIN_WIDTH
import org.kafka.common.image.Icons
import org.kafka.common.test.testTagUi
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.common.widgets.shadowMaterial
import org.kafka.item.R
import org.kafka.ui.components.material.FloatingButton
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.Dimens

@Composable
fun ItemDetailActions(
    itemId: String,
    ctaText: String,
    onPrimaryAction: (String) -> Unit,
    openFiles: (String) -> Unit,
    isFavorite: Boolean,
    showDownloads: Boolean = true,
    toggleFavorite: () -> Unit
) {
    Box(Modifier.fillMaxWidth()) {
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

            if (showDownloads) {
                Box(modifier = Modifier.weight(0.2f)) {
                    Icon(
                        icon = Icons.Download,
                        contentDescription = stringResource(R.string.cd_files),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTagUi("download_files"),
                        onClicked = { openFiles(itemId) }
                    )
                }
            }

            FloatingButton(
                text = ctaText,
                modifier = Modifier.weight(0.8f),
                onClickLabel = ctaText,
                onClicked = { onPrimaryAction(itemId) }
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
    val contentDescription =
        if (isFavorite) R.string.cd_remove_from_favorites else R.string.cd_add_to_favorites

    Box(
        modifier = modifier
            .size(50.dp)
            .shadowMaterial(Dimens.Spacing12, CircleShape)
            .background(background)
            .clickable { onClicked() }
    ) {
        IconButton(
            onClick = onClicked,
            onClickLabel = stringResource(id = contentDescription),
            modifier = Modifier
                .align(Alignment.Center)
                .testTagUi(if (isFavorite) "remove_favorite" else "add_favorite")
        ) {
            IconResource(
                imageVector = icon,
                tint = iconTint,
                contentDescription = stringResource(R.string.cd_favorite)
            )
        }
    }
}

@Composable
private fun Icon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
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
            IconResource(imageVector = icon, contentDescription = contentDescription)
        }
    }
}

@Preview
@Composable
private fun ActionsPreview() {
    AppTheme {
        ItemDetailActions(
            itemId = "123",
            ctaText = "Read",
            onPrimaryAction = {},
            openFiles = {},
            isFavorite = false,
            toggleFavorite = {}
        )
    }
}
