package com.kafka.content.compose.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.kafka.content.R
import com.kafka.ui.theme.KafkaColors

val authros = listOf(
    R.drawable.img_profile,
    R.drawable.img_author_camus_3,
    R.drawable.img_author_ghalib_1,
    R.drawable.img_author_rustaveli_1,
    R.drawable.img_author_kafka_the_castle,
    R.drawable.img_author_fitzgerald_the_great_gatsby,
    R.drawable.img_author_plato_1,
    R.drawable.img_author_karl_marx,
    R.drawable.img_author_rushdie_midnights_children
)

@Composable
fun AuthorItem(image: Int) {
        Row(modifier = Modifier.padding(20.dp)) {
            Card(
                modifier = Modifier.size(86.dp, 86.dp),
                backgroundColor = KafkaColors.surface,
                border = BorderStroke(2.dp, KafkaColors.surface),
                shape = CircleShape,
                elevation = 0.dp
            ) {
                Image(asset = imageResource(id = image), contentScale = ContentScale.Crop)
            }
        }
}
