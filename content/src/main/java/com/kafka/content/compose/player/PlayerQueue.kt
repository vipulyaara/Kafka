package com.kafka.content.compose.player

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kafka.content.R
import com.kafka.data.entities.Song
import com.kafka.data.entities.fileSubtitle
import com.kafka.ui_common.extensions.incrementTextSize
import com.kafka.ui_common.theme.KafkaColors

@ExperimentalLazyDsl
@Composable
fun PlayerQueue(queueSongs: List<Song>) {
    LazyColumn(content = {
        items(queueSongs) {
            QueueSongItem(song = it)
        }
    })
}

@Composable
fun QueueSongItem(song: Song) {
    Row(modifier = Modifier.height(miniPlayerHeight.dp).padding(12.dp)) {
        Card(
            modifier = Modifier.size(46.dp).align(Alignment.CenterVertically),
            backgroundColor = KafkaColors.surface,
            shape = RoundedCornerShape(3.dp),
            elevation = 0.dp
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                asset = vectorResource(id = R.drawable.ic_music),
                tint = KafkaColors.secondary
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().align(Alignment.CenterVertically)) {
            Text(
                text = song.title,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle2.incrementTextSize(),
                color = KafkaColors.textPrimary
            )

            Text(
                text = song.fileSubtitle,
                maxLines = 1,
                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
                color = KafkaColors.textSecondary
            )
        }

        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            asset = vectorResource(R.drawable.ic_play),
            tint = KafkaColors.iconPrimary
        )
    }
}
