package com.kafka.content.compose.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.kafka.content.compose.NetworkImage
import com.kafka.data.entities.Creator
import com.kafka.data.entities.Item
import com.kafka.ui_common.theme.KafkaColors
import com.kafka.ui_common.theme.KafkaTheme

@Composable
fun ContentItem(item: Item, modifier: Modifier = Modifier, onItemClick: (Item) -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable(onClick = { onItemClick(item) }),
        color = KafkaColors.background,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, KafkaColors.surface.copy(alpha = 0.3f)),
        elevation = 0.dp
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier.size(90.dp, 104.dp),
                backgroundColor = KafkaColors.surface,
                shape = RoundedCornerShape(3.dp),
                elevation = 0.dp
            ) {
                item.coverImage?.let { NetworkImage(url = it) }
            }
            ItemDescription(item)
        }
    }
}

@Composable
fun ItemDescription(item: Item) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = item.title.toString(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
            color = KafkaColors.textPrimary
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = item.creator?.name.orEmpty(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2,
            color = KafkaColors.textSecondary
        )

        Text(
            text = item.mediaType.orEmpty(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2,
            color = KafkaColors.secondary
        )
    }
}

@Preview
@Composable
fun ContentItemPreview() {
    KafkaTheme {
        ContentItem(
            item = Item(
                itemId = "",
                title = "Selected ghazals of Ghalib",
                creator = Creator("", "Mirza Ghalib"),
                mediaType = "audio"
            ),
            onItemClick = {}
        )
    }
}
