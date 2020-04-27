package com.kafka.player.model

data class PlaybackItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val duration: Long,
    val contentUrl: String
)
