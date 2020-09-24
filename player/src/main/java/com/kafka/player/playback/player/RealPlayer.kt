package com.kafka.player.playback.player

import android.content.Context
import com.data.base.extensions.debug
import com.data.base.extensions.e
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
    val player by lazy { SimpleExoPlayer.Builder(context).setUseLazyPreparation(true).build() }
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

            player.clearMediaItems()
            player.addMediaItems(queue.toMediaItems())
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
        player.playWhenReady = true
    }

    override fun play(song: Song) {
        launch {
            var position = queueDao.getQueueSongs().indexOfFirst { it?.id == song.id }
            if (position == -1) position = 0

            launch(Dispatchers.Main) {
                debug { "seek position $position" }
                player.prepare()
                player.play()
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
