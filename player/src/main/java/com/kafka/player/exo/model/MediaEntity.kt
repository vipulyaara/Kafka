package com.kafka.player.exo.model

internal data class MediaEntity(
    val id: String,
    val title: String,
    val subtitle: String,
    val duration: Long,
    val url: String
)
