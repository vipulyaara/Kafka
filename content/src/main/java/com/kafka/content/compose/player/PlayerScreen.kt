package com.kafka.content.compose.player

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.kafka.content.R
import com.kafka.content.ui.player.PlayerViewModel
import com.kafka.data.entities.Song
import com.kafka.player.domain.CurrentSong
import com.kafka.player.domain.PlayerCommand
import com.kafka.player.domain.isPlaying
import com.kafka.ui_common.extensions.alignCenter
import com.kafka.ui_common.theme.KafkaColors
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun PlayerWithMiniPlayer() {
    val viewModel: PlayerViewModel = viewModel()
    val viewState = viewModel.state.collectAsState().value

    Box(modifier = Modifier.background(KafkaColors.primary)) {
        viewState.currentSong?.let {
            Column {
                MiniPlayer(it)
                PlayerNowPlaying(it.song)
            }
        }
    }
}

@Composable
fun MiniPlayer(song: CurrentSong) {
    Row(modifier = Modifier.padding(12.dp).fillMaxHeight()) {
        Card(
            modifier = Modifier.size(44.dp, 44.dp),
            backgroundColor = KafkaColors.secondary,
            shape = RoundedCornerShape(3.dp),
            elevation = 2.dp
        ) {
            CoilImage(data = R.drawable.img_banner_26, contentScale = ContentScale.Crop)
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = song.song.title,
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                color = KafkaColors.surface
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = song.song.subtitle,
                maxLines = 1,
                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
                color = KafkaColors.surface.copy(alpha = 0.6f)
            )

        }

        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            asset = vectorResource(R.drawable.ic_play),
            tint = KafkaColors.background
        )
    }
}

@Composable
fun PlayerNowPlaying(song: Song) {
    Column {
        Card(
            modifier = Modifier.preferredSize(256.dp, 256.dp).align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(5.dp),
            elevation = 24.dp
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                asset = imageResource(id = R.drawable.img_banner_31)
            )
        }

        Text(
            text = song.title,
            style = MaterialTheme.typography.h2.alignCenter(),
            color = KafkaColors.background,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 24.dp).gravity(Alignment.CenterHorizontally)
        )

        Text(
            text = song.subtitle,
            style = MaterialTheme.typography.subtitle2.alignCenter(),
            color = KafkaColors.secondary,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 2.dp
            ) + Modifier.gravity(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PlayerControls(currentSong: CurrentSong, actioner: (PlayerCommand) -> Unit) {
    val playIcon = if (currentSong.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
    Row(modifier = Modifier.padding(24.dp)) {
        val iconModifier = Modifier.padding(horizontal = 12.dp).align(Alignment.CenterVertically).weight(1f)
        Icon(asset = vectorResource(R.drawable.ic_heart_sign), modifier = iconModifier)
        Icon(asset = vectorResource(R.drawable.ic_step_backward), modifier = iconModifier)
        Card(shape = CircleShape, backgroundColor = KafkaColors.secondary, modifier = iconModifier) {
            Box(modifier = Modifier.padding(13.dp).clickable(onClick = { actioner(PlayerCommand.ToggleCurrent) })) {
                Icon(asset = vectorResource(playIcon), tint = KafkaColors.background)
            }
        }
        Icon(asset = vectorResource(R.drawable.ic_skip_forward), modifier = iconModifier)
        Icon(asset = vectorResource(R.drawable.ic_shuffle), modifier = iconModifier)
    }
}

