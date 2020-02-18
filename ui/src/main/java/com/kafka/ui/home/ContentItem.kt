package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Card
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.Content
import com.kafka.ui.LoadAndShowImage
import com.kafka.ui.alignCenter

@Composable
fun ContentItem(content: Content, onItemClick: (String) -> Unit) {
    Clickable(onClick = {
        onItemClick(content.contentId)
    }) {
        Column(modifier = LayoutPadding(left = 12.dp)) {
            Card(
                modifier = LayoutSize(172.dp, 172.dp),
                shape = RectangleShape,
                elevation = 0.dp
            ) {
                if (content.coverImageResource == 0) {
                     LoadAndShowImage(data = content.coverImage ?: "")
                } else {
                    DrawImage(image = imageResource(id = content.coverImageResource))
                }
            }

            Spacer(modifier = LayoutPadding(top = 12.dp))
            Text(
                text = content.title ?: "",
                style = MaterialTheme.typography().body1.alignCenter(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = LayoutWidth(172.dp)
            )
            Text(
                text = content.creator ?: "",
                style = MaterialTheme.typography().caption.alignCenter(),
                overflow = TextOverflow.Ellipsis,
                modifier = LayoutWidth(172.dp)
            )
        }
    }
}