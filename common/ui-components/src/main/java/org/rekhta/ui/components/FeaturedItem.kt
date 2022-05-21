package org.rekhta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FeaturedItem(
    text: String,
    imageUrl: String,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(8.dp),
    size: Dp = 136.dp,
    elevation: Dp = 4.dp
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier.size(size),
            shape = shape,
            elevation = elevation
        ) {
            LoadCollectionImage(imageUrl = imageUrl)
        }
        Text(
            modifier = Modifier
                .width(size)
                .padding(horizontal = 4.dp, vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}
