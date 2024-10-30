package com.kafka.root.playback

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MiniPlayer(modifier: Modifier = Modifier, openPlaybackSheet: () -> Unit)
