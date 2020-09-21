package com.kafka.content.compose

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ContentItem(item: Item) {
    Surface(color = MaterialTheme.colors.surface) {
        Row {
            ImageCard(item)
            ItemDescription(item)
        }
    }
}

@Composable
fun ImageCard(item: Item) {
    Card(modifier = Modifier.size(96.dp)) {
        item.coverImage?.let { CoilImage(data = it) }
    }
}

@Composable
fun ItemDescription(item: Item) {
    Column {
        Text(
            text = item.title.toString(),
            maxLines = 2,
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = item.creator?.name.toString(),
            maxLines = 1,
            style = MaterialTheme.typography.body2
        )
    }
}
