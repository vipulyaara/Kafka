package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.Surface
import androidx.ui.material.contentColorFor
import androidx.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui.Clickable
import com.kafka.ui.colors
import com.kafka.ui.detail.RatingWidget
import com.kafka.ui.paddingHV
import com.kafka.ui.typography

fun randomColor() =
    arrayOf(
        Color(0xFF24aaff), Color(0xFF3D84FD), Color(0xFFCFA224), Color(0xFF2fc3c1),
        Color(0xFFFF8B0D), Color(0xFF3D84FD), Color(0xFFCFA224), Color(0xFF2fc3c1)
    ).random()

@Composable
fun ContentView(content: Item, onItemClick: (Item) -> Unit) {
    val color = colors().contentColorFor(colors().surface)
    Surface(
        modifier = Modifier.paddingHV(horizontal = 12.dp, vertical = 6.dp).fillMaxWidth(),
        color = colors().background,
        elevation = 1.dp,
        shape = RoundedCornerShape(1.dp)
    ) {
        Clickable({ onItemClick(content) }) {
            Row(modifier = Modifier.padding(16.dp)) {
                Card(modifier = Modifier.size(76.dp).gravity(Alignment.CenterVertically)) {
//                    CoilImage(data = content.coverImage ?: "", contentScale = ContentScale.Crop)
                }
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Text(text = content.title ?: "", style = typography().subtitle1, maxLines = 2)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = content.creator ?: "",
                        style = typography().body2,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    RatingWidget()
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            text = content.language?.joinToString(" • ") ?: "",
//                            style = typography().body2.copy(color = Color(0xFFC3C4C8)),
//                            maxLines = 1
//                        )
                }
            }
        }
    }
}
