package com.kafka.content.compose.player

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.content.R
import com.kafka.data.entities.Song
import com.kafka.data.entities.subtitle
import com.kafka.player.domain.CurrentSong
import com.kafka.player.domain.PlayerViewState
import com.kafka.player.domain.isPlaying
import com.kafka.ui_common.extensions.alignCenter
import com.kafka.ui_common.extensions.incrementTextSize
import com.kafka.ui_common.theme.KafkaColors
import com.kafka.ui_common.theme.KafkaTheme

@ExperimentalLazyDsl
@Composable
fun NowPlaying(viewState: PlayerViewState, alpha: Float, playerActions: PlayerActions) {
    LazyColumn(modifier = Modifier.drawLayer(alpha = alpha), content = {
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item { PlayerNowPlaying(viewState.currentSong!!.song) }
        item { Spacer(modifier = Modifier.height(12.dp)) }
        item {
            PlayerControls(
                currentSong = viewState.currentSong!!,
                isFavorite = viewState.isFavorite,
                actions = playerActions
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        items(viewState.queueSongs!!) { QueueSongItem(song = it) }
    })
}

@Composable
private fun PlayerNowPlaying(song: Song) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 240.dp)) {
//        Card(
//            modifier = Modifier.preferredSize(256.dp, 256.dp).align(Alignment.CenterHorizontally),
//            shape = RoundedCornerShape(5.dp),
//            elevation = 24.dp
//        ) {
//            NetworkImage(modifier = Modifier.fillMaxSize(), data = song.coverImage.orEmpty())
//        }

        Text(
            text = song.title,
            style = MaterialTheme.typography.h4.alignCenter(),
            color = KafkaColors.textPrimary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 32.dp).align(Alignment.CenterHorizontally)
        )

        Text(
            text = song.subtitle,
            style = MaterialTheme.typography.subtitle2.alignCenter().incrementTextSize(),
            color = KafkaColors.secondary,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 8.dp
            ).align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun PlayerControls(currentSong: CurrentSong, isFavorite: Boolean, actions: PlayerActions) {
    val playIcon = if (currentSong.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
    val favoriteIcon = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart
    val favoriteIconColor = if (isFavorite) Color(0xFFff006a) else KafkaTheme.colors.secondary

    Row(modifier = Modifier.padding(24.dp)) {
        val iconModifier = Modifier.padding(horizontal = 12.dp).align(Alignment.CenterVertically).weight(1f)
        Icon(
            asset = vectorResource(favoriteIcon),
            tint = favoriteIconColor,
            modifier = iconModifier.clickable(onClick = { actions.markFavorite() })
        )
        Icon(
            asset = vectorResource(R.drawable.ic_step_backward),
            tint = KafkaColors.secondary,
            modifier = iconModifier.clickable(
                onClick = { actions.previous() }
            )
        )
        FloatingActionButton(
            modifier = iconModifier.size(64.dp),
            backgroundColor = KafkaColors.secondary,
            onClick = { actions.togglePlayPause() }) {
            Icon(asset = vectorResource(playIcon), tint = KafkaColors.background)
        }
        Icon(
            asset = vectorResource(R.drawable.ic_skip_forward),
            tint = KafkaColors.secondary,
            modifier = iconModifier.clickable(
                onClick = { actions.next() }
            )
        )
        Icon(asset = vectorResource(R.drawable.ic_shuffle), tint = KafkaColors.secondary, modifier = iconModifier)
    }
}


data class PlayerActions(
    val togglePlayPause: () -> Unit,
    val next: () -> Unit,
    val previous: () -> Unit,
    val markFavorite: () -> Unit
)
