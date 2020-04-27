package com.kafka.player.core

import com.kafka.player.model.PlaybackItem
import javax.inject.Inject

class MusicPlayer @Inject constructor(): Player {
    override fun load(playbackItem: PlaybackItem) {
        TODO("not implemented")
    }

    override fun play() {
        TODO("not implemented")
    }

    override fun pause() {
        TODO("not implemented")
    }

    override fun stop() {
        TODO("not implemented")
    }

    override fun seekTo(seekPositionInMs: Long) {
        TODO("not implemented")
    }

    override fun destroy() {
        TODO("not implemented")
    }

    override fun setVolume(volume: Float) {
        TODO("not implemented")
    }


}
