package com.kafka.player.playback.model

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val duration: Long,
    val playbackUrl: String,
    val coverImageUrl: String,
    val isPlaying: Boolean? = null
)
