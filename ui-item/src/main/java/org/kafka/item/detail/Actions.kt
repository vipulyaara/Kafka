package org.kafka.item.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.isAudio
import com.kafka.data.entities.readerUrl
import org.kafka.common.Icons
import org.kafka.common.shadowMaterial
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.ui.components.material.FloatingButton
import ui.common.theme.theme.iconPrimary
import ui.common.theme.theme.white

@Composable
fun Actions(
    itemDetail: ItemDetail,
    openReader: (String) -> Unit,
    playAudio: (String) -> Unit,
    openFiles: (String) -> Unit,
    isFavorite: Boolean,
    shareText: () -> Unit,
    toggleFavorite: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            FloatingButton(
                text = "See Files",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            ) { openFiles(itemDetail.itemId) }

            FloatingButton(
                text = itemDetail.callToAction,
                modifier = Modifier.weight(0.5f)
            ) {
                if (itemDetail.isAudio()) {
                    playAudio(itemDetail.readerUrl())
                } else {
                    openReader(itemDetail.itemId)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FavoriteIcon(isFavorite = isFavorite) { toggleFavorite() }
            Icon(icon = Icons.Share, onClicked = shareText)
            Icon(icon = Icons.Clipboard, onClicked = shareText)
        }
    }
}

@Composable
private fun FavoriteIcon(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    val background by animateColorAsState(if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
    val iconTint by animateColorAsState(if (isFavorite) MaterialTheme.colorScheme.white else MaterialTheme.colorScheme.iconPrimary)
    val icon = if (isFavorite) Icons.HeartFilled else Icons.Heart

    Box(
        modifier = modifier
            .size(50.dp)
            .shadowMaterial(12.dp, CircleShape)
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
            .shadowMaterial(12.dp, CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClicked() }
    ) {
        IconButton(onClick = onClicked, modifier = Modifier.align(Alignment.Center)) {
            IconResource(imageVector = icon)
        }
    }
}
