package com.kafka.user.player

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.kafka.data.data.config.kodeinInstance
import com.kafka.player.model.PlaybackItem
import com.kafka.player.model.PlayerState
import com.kafka.user.extensions.logger
import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.PublishSubject
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 29/03/19.
 */
object Player {
    var playerService: PlayerService? = null
    private val notificationManager by lazy { com.kafka.user.notification.NotificationManager() }
    private val context by kodeinInstance.instance<Application>()
    private var isUnbound = false

    val playerStateObservable: PublishProcessor<PlayerState>?
    get() = playerService?.playerStateObservable

    val playerServiceConnection = PublishSubject.create<Boolean>()

    fun bindService(context: Context) {
        val intent = Intent(context, PlayerService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        isUnbound = false
    }

    fun unbindService(context: Context) {
        if (isUnbound) return
        context.unbindService(serviceConnection)
        isUnbound = true
    }

    fun playerState() = playerService?.playerState()

    fun isServiceConnected() = playerService != null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            logger.i("Player Service Disconnected")
            playerServiceConnection.onNext(false)
        }

        override fun onServiceConnected(name: ComponentName, pBinder: IBinder) {
            logger.i("Player Service Connected")
            playerService = (pBinder as PlayerService.ServiceBinder).service
            playerServiceConnection.onNext(true)
        }
    }

    fun showNotification() {
        currentItem()?.let {
            notificationManager.updateNotification(
                context,
                it,
                isPlaying()
            ) {
                // on notification update
            }
        }
    }

    fun currentItem() = playerService?.currentItem()

//    fun enqueue(item: PlaybackItem) {
//        playerService?.enqueue(item)
//    }
//
//    fun enqueue(item: PlaybackItem, song: PlaybackItem) {
//        playerService?.enqueue(item, song)
//    }

    fun load(playbackItem: PlaybackItem) {
        playerService?.load(playbackItem)
    }

    fun play(item: PlaybackItem) {
        playerService?.play(item)
    }

//    fun playNext() {
//        playerService?.playNext()
//    }
//
//    fun playPrevSong() {
//        playerService?.playPrevSong()
//    }
//
//    fun clearQueue() {
//        playerService?.clearQueue()
//    }

//    fun isPlayerEnabled(): Boolean {
//        return playerService != null
//    }

//    fun togglePlayback() {
//        playerService?.togglePlayback()
//    }

//    fun isQueueActive(): Boolean {
//        playerService?.let {
//            return it.playerQueue.queueItems()?.isEmpty() ?: false
//        }
//        return false
//    }
//
//    fun currentPlaylist(): Item? {
//        playerService?.let {
//            return it.currentPlaylist()
//        }
//        return null
//    }

    fun play() {
        playerService?.play()
    }

    fun pause() {
        playerService?.pause()
    }

//    fun seekToPosition(progress: Int) {
//        playerService?.seekToPosition(progress)
//    }

    fun isPlaying(): Boolean {
        playerService?.let {
            return it.isPlaying()
        }
        return false
    }

//    fun hasNext(): Boolean {
//        playerService?.hasNext()
//        return false
//    }
//
//    fun hasPrev(): Boolean {
//        playerService?.hasPrev()
//        return false
//    }
//
//    fun getTotalDuration(): Int {
//        playerService?.let {
//            return it.getTotalDuration()
//        }
//        return -1
//    }

//    fun stop() {
//        playerService?.stopPlayer()
//    }
//
//    fun purgePlayer() {
//        playerService?.purgePlayer()
//    }
//
//    fun getCurrentSongDuration(): Int {
//        playerService?.let {
//            return it.getCurrentDuration()
//        }
//        return -1
//    }
//
//    fun getPlayerState(): Int {
//        playerService?.let {
//            return it.getCurrentPlayerState()
//        }
//        return IPlayer.STATE_UNKNOWN
//    }
//
//    fun clearAllNotification() {
//        playerService?.let {
//            it.clearAllNotification()
//        }
//    }
//
//    var contentType: ContentType = ContentType.PLAYLIST

    var contentId: String = ""

    var packageId: String = ""
}

const val dummyUrl =
    "http://www.archive.org/download/short_story_vol017_0709_librivox/cedar_closet_hearn_rd_64kb.mp3"
