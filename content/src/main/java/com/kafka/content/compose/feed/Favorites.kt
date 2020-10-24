package com.kafka.content.compose.feed

import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui_common.theme.KafkaColors
import dev.chrisbanes.accompanist.coil.CoilImage


@Composable
fun Favorites(favorites: List<Item>, onItemClick: (String) -> Unit) {
    if (favorites.isNotEmpty()) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp).padding(top = 20.dp),
                text = "Favorites",
                style = MaterialTheme.typography.h4,
                color = KafkaColors.textPrimary
            )
            ScrollableRow(modifier = Modifier.padding(top = 8.dp, bottom = 20.dp, start = 12.dp)) {
                favorites.forEach {
                    FavoriteItem(item = it, onItemClick = onItemClick)
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(item: Item, onItemClick: (String) -> Unit) {
    val size = 164.dp
    Column(modifier = Modifier.padding(8.dp).clickable(onClick = { onItemClick(item.itemId) })) {
        Card {
            CoilImage(
                data = item.coverImage ?: "",
                fadeIn = true,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(size).clip(MaterialTheme.shapes.medium)
            )
        }

        Text(
            modifier = Modifier.padding(top = 12.dp).widthIn(0.dp, size),
            text = item.title.toString(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
            color = KafkaColors.textPrimary
        )

        Text(
            modifier = Modifier.padding(top = 4.dp).widthIn(0.dp, size),
            text = item.creator?.name.orEmpty(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2,
            color = KafkaColors.textSecondary
        )
    }
}
