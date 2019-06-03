package com.kafka.player.service

import com.kafka.player.model.PlaybackItem

interface ServiceBridge {

    fun currentSong(): PlaybackItem?

    fun enqueue(item: PlaybackItem)

    fun enqueue(item: PlaybackItem, song:PlaybackItem)

    fun playSong(item: PlaybackItem)

    fun playNextSong()

    fun playPrevSong()

    fun clearQueue()

    fun togglePlayback()

    fun currentPlaylist(): PlaybackItem?

    fun isPlaying(): Boolean

    fun pause()

    fun seekToPosition(progress: Int)
}
