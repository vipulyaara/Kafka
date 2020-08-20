package com.kafka.player.playback

import android.content.Context
import com.data.base.extensions.debug
import com.data.base.extensions.e
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.kafka.data.CustomScope
import com.kafka.player.playback.extensions.prepare
import com.kafka.player.playback.extensions.setup
import com.kafka.player.playback.extensions.toMediaSources
import com.kafka.player.playback.model.MediaItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) : Player, PlayerLifecycle, Player.Callbacks(), CoroutineScope by CustomScope() {

    private val player: SimpleExoPlayer by lazy {
        SimpleExoPlayer.Builder(context)
            .setUseLazyPreparation(true)
            .build()
    }
    private val concatenatingMediaSource by lazy { ConcatenatingMediaSource() }
    private val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))

    private fun getCurrentItem() =
        player.currentTag as? MediaItem

    override fun addPlaylistItems(
        queue: List<MediaItem>,
        clearExisting: Boolean
    ) {
        launch(Dispatchers.Main) {
            if (clearExisting) concatenatingMediaSource.clear()
            concatenatingMediaSource.addMediaSources(queue.toMediaSources(dataSourceFactory))
        }
    }

    private fun initializePlayer() {
        player.setup {
            onLoadingChange { debug { "$it" } }
            onError { e(it) { "" } }
            onTracksChanged { tracks, selections -> }
            onPositionDiscontinuity { onPositionDiscontinuity(it) }
            onTimelineChange { timeline, any -> }
            onPlayerState { playWhenReady, playbackState ->
                debug { "$playbackState" }
                currentItemFlow.value = getCurrentItem()
            }
        }

        player.prepare(concatenatingMediaSource) {
            playWhenReady = false
            shuffleModeEnabled = false
            repeatMode = REPEAT_MODE_ALL
        }
    }

    private fun onPositionDiscontinuity(reason: Int?) {
        currentItemFlow.value = getCurrentItem()
        when (reason) {
            DISCONTINUITY_REASON_PERIOD_TRANSITION -> {
            }
            DISCONTINUITY_REASON_SEEK -> {
            }
            TIMELINE_CHANGE_REASON_DYNAMIC -> {
            }
        }
    }

    override fun playCurrent() {
        player.playWhenReady = true
    }

    override fun play(mediaItem: MediaItem, position: Int) {
        launch(Dispatchers.Main) {
            player.apply {
                playWhenReady = false
                seekTo(position, C.TIME_UNSET)
                playWhenReady = true
            }
        }
    }

    override fun pause() {
        player.playWhenReady = false
    }

    override fun togglePlayPause() {
        if (player.isPlaying) {
            pause()
        } else {
            playCurrent()
        }
    }

    override fun next() {
        player.next()
    }

    override fun previous() {
        player.previous()
    }

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun onStart() {
        initializePlayer()
    }

    override fun onStop() {
        player.release()
    }

    override val callbacks: Player.Callbacks
        get() = this

    override val currentItemFlow = MutableStateFlow(getCurrentItem())
}
