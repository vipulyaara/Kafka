package com.kafka.root.playback

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.isDark

@Composable
actual fun MiniPlayer(
    modifier: Modifier,
    openPlaybackSheet: () -> Unit
) {
    com.sarahang.playback.ui.player.mini.MiniPlayer(
        useDarkTheme = LocalTheme.current.isDark(),
        modifier = modifier,
        openPlaybackSheet = openPlaybackSheet
    )
}
