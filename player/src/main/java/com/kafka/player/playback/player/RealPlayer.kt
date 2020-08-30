package com.kafka.player.playback.player

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
import com.kafka.data.dao.QueueDao
import com.kafka.data.entities.QueueEntity
import com.kafka.data.entities.Song
import com.kafka.player.playback.extensions.prepare
import com.kafka.player.playback.extensions.setup
import com.kafka.player.playback.extensions.toMediaSources
import com.kafka.player.playback.notification.NotificationManager
import com.kafka.player.timber.playback.BecomingNoisyReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val queueDao: QueueDao,
    private val notificationManager: NotificationManager,
    private val mediaSessionManager: MediaSessionManager
) : Player, CoroutineScope by CustomScope() {

    override val player: SimpleExoPlayer by lazy {
        SimpleExoPlayer.Builder(context).setUseLazyPreparation(true).build()
    }
    private val becomingNoisyReceiver = BecomingNoisyReceiver(context, mediaSessionManager.mediaSession.sessionToken)
    private val concatenatingMediaSource by lazy { ConcatenatingMediaSource() }
    private val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))

    private val currentItem
        get() = (player.currentTag as? Song)

    init {
        launch {
            if (queueDao.getQueueEntity() == null) {
                queueDao.insertQueueEntity(QueueEntity())
            }
        }
    }

    override suspend fun setQueue(queue: List<Song>) {
        queueDao.clearSongs()
        queueDao.insertAll(queue)
        concatenatingMediaSource.clear()
        concatenatingMediaSource.addMediaSources(queue.toMediaSources(dataSourceFactory))
    }

    private fun initializePlayer() {
        player.setup {
            onLoadingChange { debug { "downloading changed $it" } }
            onError { e(it) { "error" } }
            onTracksChanged { tracks, selections -> debug { "track changed" } }
            onPositionDiscontinuity { onPositionDiscontinuity(it) }
            onTimelineChange { timeline, any -> debug { "timeline changed" } }
            onSeek { debug { "seek changed" } }
            onPlayerState { playWhenReady, playbackState ->
                debug { "playing state changed $playbackState" }

                setNoisyReceiver()
                updateSeek()
            }
        }
    }

    private fun updateSeek() {
        val isPlaying = player.isPlaying
        val seek = (player.currentPosition / player.duration) * 100

        if (currentItem?.id != null) {
            launch { updateSeek(seek, isPlaying) }
        }
    }

    private fun setNoisyReceiver() {
        if (player.isPlaying) {
            becomingNoisyReceiver.register()
        } else {
            becomingNoisyReceiver.unregister()
        }
    }

    private fun updateSeek(seek: Long, isPlaying: Boolean) {
        debug { "update seek $isPlaying $seek" }
        queueDao.updatePlayingStatus(isPlaying)
        queueDao.updatePlayerSeekPosition(seek)
    }

    private fun onPositionDiscontinuity(reason: Int?) {
        debug { "onPositionDiscontinuity" }
        currentItem?.id?.let { launch { queueDao.updateCurrentSong(it) } }
        when (reason) {
            DISCONTINUITY_REASON_PERIOD_TRANSITION -> {
            }
            DISCONTINUITY_REASON_SEEK -> {
            }
            TIMELINE_CHANGE_REASON_DYNAMIC -> {
            }
        }
    }

    override fun play() {
        player.playWhenReady = true
    }

    override fun play(song: Song) {
        launch {
            var position = queueDao.getQueueSongs().indexOfFirst { it?.id == song.id }
            if (position == -1) position = 0

            launch(Dispatchers.Main) {
                debug { "seek position $position" }
                player.prepare(concatenatingMediaSource) {
                    shuffleModeEnabled = false
                    repeatMode = REPEAT_MODE_ALL

                    playWhenReady = false
                    seekTo(position, C.TIME_UNSET)
                    playWhenReady = true
                }
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
            play()
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

    override fun  start() {
        initializePlayer()
        notificationManager.attachPlayer(player)
        mediaSessionManager.connect(player)
    }

    override fun stop() {

    }

    override fun release() {
        player.release()
        notificationManager.detachPlayer()
    }
}
