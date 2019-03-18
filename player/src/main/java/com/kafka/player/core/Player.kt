package com.kafka.player.core

import com.kafka.player.model.PlaybackItem
import com.kafka.player.model.PlayerConfig

/**
 * Created by VipulKumar on 25/06/18.
 */
interface Player {
    /**
     * Initializes the player. Initialization does not play the content automatically.
     */
    fun load(playbackItem: PlaybackItem, playerConfig: PlayerConfig)

    fun play()

    fun pause()

    fun stop()

    fun seekTo(seekPositionInMs: Long)

    fun destroy()

    fun setVolume(volume: Float)
}
