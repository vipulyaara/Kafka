package com.kafka.player.exo.player

import android.content.Context
import android.util.Log
import androidx.annotation.CallSuper
import androidx.core.math.MathUtils.clamp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.kafka.player.BuildConfig
import com.kafka.player.exo.ExoPlayerListenerWrapper
import com.kafka.player.exo.IPlayerDelegate
import com.kafka.player.exo.model.PlayerMediaEntity
import com.kafka.ui_common.extensions.showToast

internal interface ISourceFactory {
    fun get(model: PlayerMediaEntity) : MediaSource
}

internal abstract class AbsPlayer(
    private val context: Context,
    lifecycle: Lifecycle,
    private val mediaSourceFactory: ISourceFactory

) : IPlayerDelegate,
    ExoPlayerListenerWrapper,
    DefaultLifecycleObserver {

    private val trackSelector = DefaultTrackSelector()
    private val factory = DefaultRenderersFactory(context).apply {
        setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)

    }
    protected val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, factory, trackSelector)

    init {
        lifecycle.addObserver(this)
    }

    @CallSuper
    override fun onDestroy(owner: LifecycleOwner) {
        player.release()
    }

    @CallSuper
    override fun prepare(mediaEntity: PlayerMediaEntity, bookmark: Long) {
        val mediaSource = mediaSourceFactory.get(mediaEntity)
        player.prepare(mediaSource)
        player.playWhenReady = false
        player.seekTo(bookmark)
    }

    @CallSuper
    override fun play(mediaEntity: PlayerMediaEntity, hasFocus: Boolean, isTrackEnded: Boolean) {
        val mediaSource = mediaSourceFactory.get(mediaEntity)
        player.prepare(mediaSource, true, true)
            player.seekTo(mediaEntity.bookmark)
        player.playWhenReady = hasFocus
    }

    @CallSuper
    override fun resume() {
        player.playWhenReady = true
    }

    @CallSuper
    override fun pause() {
        player.playWhenReady = false
    }

    @CallSuper
    override fun seekTo(where: Long) {
        val safeSeek = clamp(where, 0L, getDuration())
        player.seekTo(safeSeek)
    }

    @CallSuper
    override fun isPlaying(): Boolean {
        return player.playWhenReady
    }

    @CallSuper
    override fun getBookmark(): Long {
        return player.currentPosition
    }

    @CallSuper
    override fun getDuration(): Long {
        return player.duration
    }

    @CallSuper
    override fun setVolume(volume: Float) {
        player.volume = volume
    }

    @CallSuper
    override fun onPlayerError(error: ExoPlaybackException) {
        val what = when (error.type) {
            ExoPlaybackException.TYPE_SOURCE -> error.sourceException.message
            ExoPlaybackException.TYPE_RENDERER -> error.rendererException.message
            ExoPlaybackException.TYPE_UNEXPECTED -> error.unexpectedException.message
            else -> "Unknown: $error"
        }
        error.printStackTrace()

        if (BuildConfig.DEBUG) {
            Log.e("Player", "onPlayerError $what")
        }
        context.applicationContext.showToast("Player error")
    }

}
