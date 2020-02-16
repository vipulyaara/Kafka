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

@Composable
fun ContentItem(content: Content, onItemClick: (String) -> Unit) {
    Clickable(onClick = {
        onItemClick(content.contentId)
    }) {
        Column {
            Card(
                modifier = LayoutSize(164.dp, 164.dp) + LayoutPadding(left = 12.dp),
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
                style = MaterialTheme.typography().body2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = LayoutPadding(left = 16.dp) + LayoutWidth.Max(150.dp)
            )
            Text(
                text = content.creator ?: "",
                style = MaterialTheme.typography().caption,
                modifier = LayoutPadding(left = 16.dp)
            )
        }
    }
}