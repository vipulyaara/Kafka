package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.layout.preferredSize
import androidx.ui.layout.preferredWidth
import androidx.ui.material.Card
import androidx.ui.material.MaterialTheme
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui.lineHeight
import com.kafka.ui.paddingHV
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ContentItem(content: Item, onItemClick: (Item) -> Unit) {
    val size = 184.dp
    Clickable(onClick = { onItemClick(content) }) {
        Column(modifier = Modifier.padding(12.dp)) {
            Card(
                modifier = Modifier.preferredSize(size),
                shape = RoundedCornerShape(2.dp),
                elevation = 6.dp
            ) {
                CoilImage(data = content.coverImage ?: "", contentScale = ContentScale.Crop)
            }

            Text(
                text = content.title ?: "",
                modifier = Modifier.preferredWidth(size).paddingHV(horizontal = 8.dp, vertical = 12.dp),
                maxLines = 2,
                style = MaterialTheme.typography.subtitle2.lineHeight(1.3),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
