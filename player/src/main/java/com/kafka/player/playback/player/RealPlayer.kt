package com.kafka.player.playback.player

import android.content.Context
import com.kafka.data.extensions.debug
import com.kafka.data.extensions.e
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.kafka.data.CustomScope
import com.kafka.data.dao.QueueDao
import com.kafka.data.entities.QueueEntity
import com.kafka.data.entities.Song
import com.kafka.player.playback.extensions.setup
import com.kafka.player.playback.extensions.toMediaItems
import com.kafka.player.playback.notification.NotificationManager
import com.kafka.player.timber.playback.BecomingNoisyReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val queueDao: QueueDao,
    private val notificationManager: NotificationManager,
    mediaSessionManager: MediaSessionManager
) : Player, CoroutineScope by CustomScope() {
    val player by lazy { SimpleExoPlayer.Builder(context).build() }
    private val becomingNoisyReceiver = BecomingNoisyReceiver(context, mediaSessionManager.mediaSession.sessionToken)

    private val currentItem
        get() = player.currentMediaItem

    init {
        launch {
            if (queueDao.getQueueEntity() == null) {
                queueDao.insertQueueEntity(QueueEntity())
            }
        }
    }

    override fun setQueue(queue: List<Song>) {
        launch {
            queueDao.clearSongs()
            queueDao.insertAll(queue)

            debug { "Before" }

            launch(Dispatchers.Main) {
                player.clearMediaItems()
                player.addMediaItems(queue.toMediaItems())
                debug { "Middle" }
            }
            debug { "After" }
        }
    }

    private fun initializePlayer() {
        player.setup {
            onLoadingChange { debug { "loading changed $it" } }
            onError { e(it) { "error" } }
            onTracksChanged { tracks, selections -> debug { "track changed" } }
            onTimelineChange { timeline, any -> debug { "timeline changed" } }
            onSeek { debug { "seek changed" } }
            onPlayerState { playWhenReady, playbackState ->
                debug { "playing state changed $playbackState" }
                setNoisyReceiver()
                updateSeek()
            }

            onMediaItemChanged {
                debug { "media item changed" }
                it?.mediaId?.let { launch { queueDao.updateCurrentSong(it) } }
            }
        }
    }

    private fun updateSeek() {
        val isPlaying = player.isPlaying
        val seek = (player.currentPosition / player.duration) * 100

        if (currentItem?.mediaId != null) {
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

    override fun play() {
        debug { "play current" }
        launch(Dispatchers.Main) {
            player.playWhenReady = true
        }
    }

    override suspend fun play(position: Int) {
        coroutineScope {
            debug { "play song" }
            launch(Dispatchers.Main) {
                debug { "playing item from queue $position" }
                player.seekTo(/* windowIndex= */ position, C.TIME_UNSET)
                player.prepare()
                play()
            }
        }
    }

    override fun pause() {
        player.playWhenReady = false
    }

    override fun togglePlayPause() {
        debug { "toggle play pause ${player.playbackState}" }
        launch(Dispatchers.Main) {
            if (player.isPlaying) {
                pause()
            } else {
                player.play()
            }
        }
    }

    override fun next() {
        launch(Dispatchers.Main) {
            player.next()
        }
    }

    override fun previous() {
        launch(Dispatchers.Main) {
            player.previous()
        }
    }

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun start() {
        initializePlayer()
        notificationManager.attachPlayer(player)
//        mediaSessionManager.connect(player)
    }

    override fun stop() {

    }

    override fun release() {
        player.release()
        notificationManager.detachPlayer()
    }
}
