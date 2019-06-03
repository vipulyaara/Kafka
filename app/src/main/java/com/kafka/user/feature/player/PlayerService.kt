package com.kafka.user.feature.player

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.kafka.data.entities.Item
import com.kafka.data.extensions.disposeOnDestroy
import com.kafka.player.core.AudioPlayer
import com.kafka.player.model.PlaybackItem
import com.kafka.player.model.PlayerConfig
import com.kafka.player.model.PlayerState
import com.kafka.player.notification.NotificationChannelManager
import com.kafka.user.extensions.logger
import io.reactivex.schedulers.Schedulers

/**
 * @author Vipul Kumar; dated 29/03/19.
 */
class PlayerService : LifecycleService() {

    private lateinit var nm: NotificationManager

    private val url =
        "http://www.archive.org/download/short_story_vol017_0709_librivox/cedar_closet_hearn_rd_64kb.mp3"
    private var player: AudioPlayer? = null
    private var lastPlayerState: PlayerState = PlayerState.Stopped(false)

    override fun onCreate() {
        super.onCreate()

        nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        initPlayer()
    }

    fun isPlaying() = lastPlayerState is PlayerState.Playing

    private fun initPlayer() {
        player = AudioPlayer()
        player?.playerStateObservable?.observeOn(Schedulers.io())
            ?.subscribe {
                lastPlayerState = it
                logger.d("State $it") }
            ?.disposeOnDestroy(this)

        loadUrl(url)
    }

    private fun loadUrl(url: String) {
        player?.load(PlaybackItem(url), PlayerConfig(""))
        player?.play()
    }

    private val binder: IBinder = PlayerBinder()

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return binder
    }

//    fun getConnection(): ConnectionRecord {
//        return myConnection
//    }

    inner class PlayerBinder : Binder() {
        internal val service: PlayerService
            get() = this@PlayerService
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            showNotification(it)
//            handleIntent(it)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var notification: Notification? = null

    private fun showNotification(intent: Intent) {
        notification?.let {
            if (lastPlayerState is PlayerState.Playing) {
                startForeground(100, it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

//    private fun showNotification(playerState: Int) {
//        player?.let {
//            updateNotification(
//                this,
//                it,
//                getMediaSessionToken(),
//                playerState,
//                object : NotificationChannelManager.OnNotificationUpdateListener {
//                    override fun onNotificationUpdate(notification: Notification) {
//                        this@PlayerService.notification = notification
//                        when (playerState) {
//                            IPlayer.STATE_STOPPED, IPlayer.STATE_PAUSED, IPlayer.ERROR_UNKNOWN -> {
//                                stopForeground(false)
//                            }
//                            IPlayer.STATE_PREPARING -> {    //In FUPLimit case, we have started foreground but has not startForeground so separated this case
//                            }
//                            else -> {
//                                startServiceAsForeground()
//                            }
//                        }
//                        nm.notify(NOTIFICATION_ID, notification)
//                    }
//                })
//        }
//    }
//
//    private fun handleIntent(intent: Intent?) {
//        intent?.action?.let {
//            when (it) {
//                PlayerConstants.PlayerCommand.TOGGLE.toString() -> {
//                    togglePlayback()
//                }
//                PlayerConstants.PlayerCommand.NEXT.toString() -> {
//                    if (playerQueue.hasNext())
//                        playNextSong()
//                }
//                PlayerConstants.PlayerCommand.PREV.toString() -> {
//                    if (playerQueue.hasPrev())
//                        playPrevSong()
//                }
//                PlayerConstants.PlayerCommand.STOP.toString() -> {
//                    if(!isPlaying()) {
//                        player?.stop()
//                    }
//                }
//            }
//        }
//    }

    fun play() {
        player?.play()
    }

    fun pause() {
        player?.pause()
    }

    private fun clearNotification() {
        nm.cancel(100)
        stopForeground(true)
    }

    private fun release() {
        player?.destroy()
        player = null
    }
}
