package com.kafka.player.exo

import android.content.Context
import com.data.base.extensions.debug
import com.data.base.extensions.e
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.kafka.player.exo.extensions.prepare
import com.kafka.player.exo.extensions.setup
import com.kafka.player.exo.extensions.toMediaSource
import com.kafka.player.exo.extensions.toMediaSources
import com.kafka.player.exo.model.MediaItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealPlayer @Inject constructor(@ApplicationContext private val context: Context) : Player, PlayerLifecycle {
    private lateinit var player: SimpleExoPlayer
    private val playlist by lazy { ConcatenatingMediaSource() }
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
    }

    override fun onStart() {
        initializePlayer()
    }

    override fun onStop() {
        player.release()
    }

    override fun addPlaylistItems(queue: List<MediaItem>) {
        playlist.addMediaSources(queue.toMediaSources(dataSourceFactory))

        player.prepare(playlist) {
            playWhenReady = true
            shuffleModeEnabled = false
            repeatMode = REPEAT_MODE_ALL
        }
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(context).build()

        player.setup {
            onLoadingChange {
                debug { "$it" }
            }
            onError {
                e(it) { "" }
            }
            onTracksChanged { tracks, selections -> }
            onPositionDiscontinuity { onPositionDiscontinuity(it) }
            onPlayerState { playWhenReady, playbackState ->
                debug { "$playbackState" }
            }
        }
    }

    private fun onPositionDiscontinuity(reason: Int?) {
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

    override fun play(mediaItem: MediaItem) {
        playlist.addMediaSource(0, mediaItem.toMediaSource(dataSourceFactory))
        playCurrent()
    }

    override fun pause() {
        player.playWhenReady = false
    }

    override fun togglePlayPause() {
        TODO("not implemented")
    }

    override fun next() {
        TODO("not implemented")
    }

    override fun previous() {
        TODO("not implemented")
    }

    override fun seekTo() {
        TODO("not implemented")
    }

}
