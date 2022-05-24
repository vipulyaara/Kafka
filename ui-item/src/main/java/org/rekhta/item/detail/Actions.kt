package org.rekhta.item.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.readerUrl
import org.kafka.common.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.rekhta.ui.components.material.FloatingButton
import org.rekhta.ui_common_compose.shadowMaterial
import ui.common.theme.theme.iconPrimary
import ui.common.theme.theme.white

@Composable
fun Actions(
    itemDetail: ItemDetail,
    openReader: (String) -> Unit,
    isFavorite: Boolean,
    shareText: () -> Unit,
    toggleFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        FavoriteIcon(isFavorite = isFavorite) { toggleFavorite() }
        ShareIcon(onClicked = shareText)
        FloatingButton(
            text = itemDetail.callToAction.uppercase(),
            modifier = Modifier.weight(0.5f)
        ) {
            openReader(itemDetail.readerUrl())
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
private fun ShareIcon(
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
            IconResource(imageVector = Icons.Share)
        }
    }
}
