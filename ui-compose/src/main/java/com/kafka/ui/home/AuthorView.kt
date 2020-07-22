package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.layout.size
import androidx.ui.material.Card
import androidx.ui.unit.dp
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.ui.*
import dev.chrisbanes.accompanist.coil.CoilImage

data class Author(val name: String)

@Composable
fun AuthorItem(author: Author) {
    Card(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(4.dp),
        color = colors().background,
        elevation = 0.dp,
        border = Border(1.dp, colors().surface)
    ) {
        val icon = getRandomAuthorResource()
        Column(modifier = Modifier.padding(24.dp)) {
            Card(
                modifier = Modifier.size(96.dp).gravity(Alignment.CenterHorizontally),
                shape = CircleShape,
                elevation = 6.dp,
                border = Border(1.dp, colors().surface)
            ) {
                CoilImage(data = icon)
            }
            Text(
                text = author.name,
                style = typography().subtitle2.alignCenter(),
                modifier = Modifier.padding(top = 12.dp).gravity(Alignment.CenterHorizontally),
                color = colors().onPrimary
            )
            Text(
                text = "1772 - 1856",
                style = typography().body1.alignCenter().decrementTextSize(4),
                modifier = Modifier.padding(top = 4.dp).gravity(Alignment.CenterHorizontally),
                color = colors().onPrimary.alpha(alpha = 0.8f)
            )
        }
    }
}
