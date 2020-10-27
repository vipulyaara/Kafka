package com.kafka.content.compose.player

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.drawLayer
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.kafka.content.R
import com.kafka.content.compose.NetworkImage
import com.kafka.content.ui.detail.ItemDetailViewModel
import com.kafka.content.ui.player.PlayerViewModel
import com.kafka.data.entities.Song
import com.kafka.data.entities.subtitle
import com.kafka.data.extensions.debug
import com.kafka.player.domain.PlayerCommand
import com.kafka.ui_common.navigation.backHandler
import com.kafka.ui_common.theme.KafkaColors
import com.kafka.ui_common.widget.lerp

const val miniPlayerHeight = 72

@ExperimentalLazyDsl
@ExperimentalMaterialApi
@Composable
fun MiniPlayerScaffold(modifier: Modifier, content: @Composable (PaddingValues) -> Unit) {
    WithConstraints {
        val dragRange = constraints.maxHeight - miniPlayerHeight
        val sheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)

        backHandler(
            enabled = sheetState.value == BottomSheetValue.Expanded,
            onBack = { sheetState.animateTo(BottomSheetValue.Collapsed) }
        )

        val openFraction = if (sheetState.offset.value.isNaN()) {
            0f
        } else {
            sheetState.offset.value / dragRange
        }.coerceIn(0f, 1f)

        debug { "lerp1 ${sheetState.offset.value} ${dragRange} $openFraction" }

        BottomSheetScaffold(
            modifier = modifier,
            sheetPeekHeight = miniPlayerHeight.dp,
            scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = sheetState
            ),
            sheetContent = {
                debug { "lerp2 ${sheetState.offset.value} ${dragRange} $openFraction" }
                Player(openFraction) { sheetState.animateTo(it) }
            },
            bodyContent = content
        )
    }
}

@ExperimentalLazyDsl
@ExperimentalMaterialApi
@Composable
fun Player(openFraction: Float, updateSheet: (BottomSheetValue) -> Unit) {
    val viewModel: PlayerViewModel = viewModel()
    val itemDetailViewModel: ItemDetailViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    val alpha = lerp(0f, 1f, 0.2f, 0.8f, 1 - openFraction)

    Box(modifier = Modifier.background(KafkaColors.surface)) {
        viewState.currentSong?.let {
            Column(modifier = Modifier.animateContentSize()) {
                MiniPlayer(it.song, alpha, updateSheet)
                NowPlaying(viewState, alpha, PlayerActions(
                    togglePlayPause = { viewModel.command(PlayerCommand.ToggleCurrent) },
                    next = { viewModel.command(PlayerCommand.Next) },
                    previous = { viewModel.command(PlayerCommand.Previous) },
                    markFavorite = { itemDetailViewModel.updateFavorite(it.song.itemId) }
                ))
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MiniPlayer(song: Song, alpha: Float, updateSheet: (BottomSheetValue) -> Unit) {
    val cornerSize = lerp(50f, 2f, 0.2f, 0.8f, alpha)

    Row(modifier = Modifier.height(miniPlayerHeight.dp).padding(12.dp).clickable(onClick = {
        updateSheet(BottomSheetValue.Expanded)
    })) {
        Card(
            modifier = Modifier.size(46.dp)
                .align(Alignment.CenterVertically)
                .drawLayer(
                    translationY = alpha * 700,
                    translationX = alpha * 600,
                    scaleX = (alpha * 5).coerceAtLeast(1f),
                    scaleY = (alpha * 5).coerceAtLeast(1f)
                ),
            backgroundColor = KafkaColors.secondary,
            shape = RoundedCornerShape(cornerSize.toInt()),
            elevation = 2.dp
        ) {
            NetworkImage(data = song.coverImage.orEmpty())
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp).drawLayer(alpha = 1 - alpha).fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = song.title,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle2,
                color = KafkaColors.textPrimary
            )

            Text(
                text = song.subtitle,
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

