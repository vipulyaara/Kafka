package com.kafka.player.playback.player

import com.kafka.data.entities.Song

interface Player {
    fun play()
    fun play(song: Song)
    fun pause()
    fun togglePlayPause()
    fun next()
    fun previous()
    fun seekTo(position: Long)
    fun setQueue(queue: List<Song>)
    fun start()
    fun stop()
    fun release()
}

