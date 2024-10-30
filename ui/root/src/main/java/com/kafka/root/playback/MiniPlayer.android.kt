package com.kafka.root.playback

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.shouldUseDarkColors

@Composable
actual fun MiniPlayer(
    modifier: Modifier,
    openPlaybackSheet: () -> Unit
) {
    com.sarahang.playback.ui.player.mini.MiniPlayer(
        useDarkTheme = LocalTheme.current.shouldUseDarkColors(),
        modifier = modifier,
        openPlaybackSheet = openPlaybackSheet
    )
}
