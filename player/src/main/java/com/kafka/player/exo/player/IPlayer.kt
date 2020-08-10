package com.kafka.player.exo.player

import android.support.v4.media.session.PlaybackStateCompat
import com.kafka.player.exo.model.MediaEntity
import com.kafka.player.exo.model.PlayerMediaEntity

internal interface IPlayer : IPlayerLifecycle {

    fun prepare(playerModel: PlayerMediaEntity)
    fun playNext(playerModel: PlayerMediaEntity)
    fun play(playerModel: PlayerMediaEntity)

    fun seekTo(millis: Long)

    fun resume()
    fun pause(stopService: Boolean, releaseFocus: Boolean = true)

    fun isPlaying(): Boolean
    fun getBookmark(): Long

    fun forwardTenSeconds()
    fun replayTenSeconds()

    fun forwardThirtySeconds()
    fun replayThirtySeconds()

    fun stopService()

    fun setVolume(volume: Float)
}


internal interface IPlayerLifecycle {

    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    interface Listener {
        fun onPrepare(metadata: MediaEntity) {}
        fun onMetadataChanged(metadata: MediaEntity) {}
        fun onStateChanged(state: PlaybackStateCompat) {}
        fun onSeek(where: Long) {}
    }

}
