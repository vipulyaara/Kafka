package com.kafka.player.exo

import com.kafka.player.exo.model.MediaItem

interface Player {
    fun playCurrent()
    fun play(mediaItem: MediaItem)
    fun pause()
    fun togglePlayPause()
    fun next()
    fun previous()
    fun seekTo()
    fun addPlaylistItems(queue: List<MediaItem>)
}
