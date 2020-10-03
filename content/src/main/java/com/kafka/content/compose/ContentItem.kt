package com.kafka.content.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.kafka.data.entities.Creator
import com.kafka.data.entities.Item
import com.kafka.ui.theme.KafkaTheme

@Composable
fun ContentItem(item: Item, modifier: Modifier = Modifier, onItemClick: (Item) -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable(onClick = { onItemClick(item) }),
        color = KafkaTheme.colors.background,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, KafkaTheme.colors.surface.copy(alpha = 0.3f)),
        elevation = 0.dp
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier.size(84.dp, 96.dp),
                backgroundColor = KafkaTheme.colors.surface,
                shape = RoundedCornerShape(6.dp),
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
        ProvideEmphasis(EmphasisAmbient.current.high) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                text = item.title.toString(),
                maxLines = 2,
                style = MaterialTheme.typography.body1,
                color = KafkaTheme.colors.textPrimary
            )
        }

        ProvideEmphasis(EmphasisAmbient.current.medium) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = item.creator?.name.orEmpty(),
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                color = KafkaTheme.colors.textSecondary
            )
        }

        ProvideEmphasis(EmphasisAmbient.current.medium) {
            Text(
                text = item.mediaType.orEmpty(),
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                color = KafkaTheme.colors.secondary
            )
        }
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
