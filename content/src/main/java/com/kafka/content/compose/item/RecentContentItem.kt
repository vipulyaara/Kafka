package com.kafka.content.compose.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.kafka.content.compose.NetworkImage
import com.kafka.data.entities.Creator
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemWithRecentItem
import com.kafka.data.entities.RecentItem
import com.kafka.ui.theme.KafkaColors
import com.kafka.ui.theme.KafkaTheme
import decrementTextSize

@Composable
fun RecentContentItem(recent: ItemWithRecentItem, modifier: Modifier = Modifier, onItemClick: (Item) -> Unit) {
    val item = recent.item
    Box(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable(onClick = { onItemClick(item) })
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp)) {
                Card(
                    modifier = Modifier.size(76.dp, 88.dp),
                    backgroundColor = KafkaColors.surface,
                    border = BorderStroke(2.dp, KafkaColors.background),
                    shape = RoundedCornerShape(2.dp),
                    elevation = 0.dp
                ) {
                    item.coverImage?.let { NetworkImage(url = it) }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            text = item.title.toString(),
                            maxLines = 2,
                            style = MaterialTheme.typography.body1.decrementTextSize(1),
                            color = KafkaColors.textPrimary
                        )

                        Text(
                            text = item.mediaType.orEmpty(),
                            maxLines = 1,
                            style = MaterialTheme.typography.body2,
                            color = KafkaColors.secondary
                        )

                    RecentProgressBar(modifier = Modifier.padding(top = 12.dp))
                }
            }

            Surface(
                modifier = Modifier.height(4.dp).fillMaxWidth().padding(vertical = 12.dp),
                elevation = 8.dp,
                color = KafkaColors.secondary,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = item.mediaType.orEmpty(),
                    maxLines = 1,
                    style = MaterialTheme.typography.body2,
                    color = KafkaColors.secondary
                )
            }
        }
    }
}

@Composable
fun RecentProgressBar(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().height(2.dp)
            .background(color = KafkaColors.secondary, shape = RoundedCornerShape(12.dp))
    )
}

@Preview
@Composable
fun RecentContentItemPreview() {
    KafkaTheme {
        RecentContentItem(
            recent = ItemWithRecentItem(
                Item(
                    itemId = "",
                    title = "Selected ghazals of Ghalib",
                    creator = Creator("", "Mirza Ghalib"),
                    mediaType = "audio"
                ), RecentItem("", 0L)
            ), onItemClick = {}
        )
    }
}
