package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.MaterialTheme
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui.alignCenter
import com.kafka.ui.graphics.LoadNetworkImage

@Composable
fun ContentItem(content: Item, onItemClick: (String) -> Unit) {
    Clickable(onClick = {
        onItemClick(content.contentId)
    }) {
        Column(modifier = LayoutPadding(start = 12.dp)) {
            Card(
                modifier = LayoutSize(164.dp, 164.dp),
                shape = RectangleShape,
                elevation = 0.dp
            ) {
                if (content.coverImageResource == 0) {
                    LoadNetworkImage(data = content.coverImage ?: "")
                } else {
                    SimpleImage(image = imageResource(id = content.coverImageResource))
                }
            }

            Spacer(modifier = LayoutPadding(top = 12.dp))
            Text(
                text = content.title ?: "",
                modifier = LayoutWidth(164.dp),
                maxLines = 2,
                style = MaterialTheme.typography.body1.alignCenter(),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = content.creator ?: "",
                modifier = LayoutWidth(164.dp),
                style = MaterialTheme.typography.caption.alignCenter(),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
