package com.kafka.player.exo

import com.kafka.player.exo.model.PlayerMediaEntity

internal interface IPlayerDelegate {

    fun prepare(mediaEntity: PlayerMediaEntity, bookmark: Long)

    fun play(mediaEntity: PlayerMediaEntity, hasFocus: Boolean, isTrackEnded: Boolean)

    fun seekTo(where: Long)

    fun resume()
    fun pause()

    fun isPlaying(): Boolean
    fun getBookmark(): Long

    fun getDuration(): Long

    fun setVolume(volume: Float)

    fun setPlaybackSpeed(speed: Float)

}
