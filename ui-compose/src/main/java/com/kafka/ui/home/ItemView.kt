package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.MaterialTheme
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui.alpha
import com.kafka.ui.colors
import com.kafka.ui.incrementTextSize
import com.kafka.ui.lineHeight
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ContentItem(content: Item, onItemClick: (Item) -> Unit) {
    val size = 164.dp
    Column(
        modifier = Modifier.padding(8.dp).clickable(onClick = { onItemClick(content) })
    ) {
        Card(
            modifier = Modifier.preferredSize(size),
            shape = RoundedCornerShape(4.dp),
            elevation = 1.dp
        ) {
            CoilImage(data = content.coverImageResource, contentScale = ContentScale.Crop)
        }

        Text(
            text = content.title ?: "",
            modifier = Modifier.preferredWidth(size).padding(horizontal = 8.dp, vertical = 12.dp),
            maxLines = 2,
            style = MaterialTheme.typography.subtitle2.lineHeight(1.3).incrementTextSize(),
            color = colors().onPrimary,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ContentItemList(content: Item, onItemClick: (Item) -> Unit) {
    Stack(modifier = Modifier.clickable(onClick = { onItemClick(content) }, indication = null)) {
        Row(modifier = Modifier.padding(top = 36.dp, start = 20.dp, end = 20.dp, bottom = 2.dp).fillMaxWidth()) {
            Card(
                modifier = Modifier.preferredSize(106.dp, 106.dp).gravity(Alignment.CenterVertically),
                shape = RoundedCornerShape(2.dp),
                elevation = 1.dp
            ) {
                CoilImage(data = content.coverImage ?: "", contentScale = ContentScale.Crop)
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = content.title ?: "",
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 2,
                    style = MaterialTheme.typography.subtitle1.lineHeight(1.1),
                    color = colors().onPrimary,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = content.creator ?: "",
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 2,
                    color = colors().onPrimary.alpha(alpha = 0.5f),
                    style = MaterialTheme.typography.caption.alpha(alpha = 0.4f).incrementTextSize()
                )
            }
        }
    }
}
