package com.kafka.player.playback.extensions

import com.kafka.data.extensions.debug
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

class PlayerListenerBuilder {
    private var onPlaybackParameters: ((PlaybackParameters) -> Unit)? = null
    private var onSeek: (() -> Unit)? = null
    private var onTracksChanged: ((TrackGroupArray, TrackSelectionArray) -> Unit)? = null
    private var onError: ((ExoPlaybackException) -> Unit)? = null
    private var onLoadingChange: ((Boolean) -> Unit)? = null
    private var onShuffleChange: ((Boolean) -> Unit)? = null
    private var onPositionDiscontinuity: ((Int) -> Unit)? = null
    private var onRepeatModeChange: ((Int?) -> Unit)? = null
    private var onTimelineChange: ((Timeline?, Int) -> Unit)? = null
    private var onPlayerState: ((Boolean, Int) -> Unit)? = null
    private var onMediaItemChanged: ((MediaItem?) -> Unit)? = null

    fun onPlaybackParameters(callback: ((PlaybackParameters?) -> Unit)) {
        onPlaybackParameters = callback
    }

    fun onSeek(callback: (() -> Unit)) {
        onSeek = callback
    }

    fun onTracksChanged(callback: (TrackGroupArray?, TrackSelectionArray?) -> Unit) {
        onTracksChanged = callback
    }

    fun onError(callback: (ExoPlaybackException?) -> Unit) {
        onError = callback
    }

    fun onLoadingChange(callback: (Boolean?) -> Unit) {
        onLoadingChange = callback
    }

    fun onShuffleChange(callback: (Boolean?) -> Unit) {
        onShuffleChange = callback
    }

    fun onPositionDiscontinuity(callback: (Int?) -> Unit) {
        this.onPositionDiscontinuity = callback
    }

    fun onRepeatModeChange(callback: (Int?) -> Unit) {
        onRepeatModeChange = callback
    }

    fun onTimelineChange(callback: (Timeline?, Any?) -> Unit) {
        onTimelineChange = callback
    }

    fun onPlayerState(callback: (Boolean, Int) -> Unit) {
        onPlayerState = callback
    }

    fun onMediaItemChanged(callback: (MediaItem?) -> Unit) {
        onMediaItemChanged = callback
    }

    fun build(): Player.EventListener {
        return object : Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                onPlaybackParameters?.invoke(playbackParameters)
            }

            override fun onSeekProcessed() {
                debug { "Seek is" }
                onSeek?.invoke()
            }

            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
                onTracksChanged?.invoke(trackGroups, trackSelections)
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                onError?.invoke(error)
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                onLoadingChange?.invoke(isLoading)
            }

            override fun onPositionDiscontinuity(reason: Int) {
                onPositionDiscontinuity?.invoke(reason)
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                onRepeatModeChange?.invoke(repeatMode)
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                onShuffleChange?.invoke(shuffleModeEnabled)
            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                onTimelineChange?.invoke(timeline, reason)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                onPlayerState?.invoke(playWhenReady, playbackState)
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                onMediaItemChanged?.invoke(mediaItem)
            }
        }
    }
}

fun Player.setup(configure: PlayerListenerBuilder.() -> Unit) {
    val builder = PlayerListenerBuilder()
    builder.configure()
    this.addListener(builder.build())
}
