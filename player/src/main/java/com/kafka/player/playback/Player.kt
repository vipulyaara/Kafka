package com.kafka.player.playback

import com.kafka.player.playback.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface Player {
    fun playCurrent()
    fun play(mediaItem: MediaItem, position: Int)
    fun pause()
    fun togglePlayPause()
    fun next()
    fun previous()
    fun seekTo(position: Long)
    fun addPlaylistItems(queue: List<MediaItem>, clearExisting: Boolean = true)
    val callbacks: Callbacks

    abstract class Callbacks {
        abstract val currentItemFlow: Flow<MediaItem?>
    }
}

