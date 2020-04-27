package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.MaterialTheme
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui.graphics.LoadNetworkImage

@Composable
fun Banner(item: Item) {
    Row(modifier = Modifier.padding(start = 12.dp)) {
        Card(
            modifier = Modifier.preferredSize(156.dp, 156.dp) + Modifier.padding(12.dp),
            shape = RoundedCornerShape(4.dp),
            elevation = 2.dp
        ) {
            if (item.coverImageResource == 0) {
                LoadNetworkImage(data = item.coverImage ?: "")
            } else {
                SimpleImage(image = imageResource(id = item.coverImageResource))
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.title ?: "",
                maxLines = 2,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.h5
            )
            Text(
                text = item.description ?: "",
                modifier = Modifier.padding(4.dp),
                maxLines = 2,
                style = MaterialTheme.typography.body2
            )
        }
    }
}
