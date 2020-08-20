package com.kafka.player.playback

import com.kafka.player.playback.model.MediaItem

interface PlayerContract {
    suspend fun isPlaying(): Boolean
    suspend fun next()
    suspend fun release()
    suspend fun hasNext(): Boolean
    suspend fun clear()
    suspend fun isEmpty(): Boolean
    suspend fun addToQueue(mediaItem: MediaItem, playWhenReady: Boolean, clearOldPlayList: Boolean)
    suspend fun addToQueue(mediaItems: List<MediaItem>, playWhenReady: Boolean, clearOldPlayList: Boolean)
    suspend fun initPlayer()
}
