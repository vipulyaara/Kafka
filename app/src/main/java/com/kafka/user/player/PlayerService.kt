//package com.kafka.user.player
//
//import android.app.Notification
//import android.app.NotificationManager
//import android.content.Context
//import android.content.Intent
//import android.os.Binder
//import android.os.IBinder
//import androidx.lifecycle.LifecycleService
//import com.kafka.player.core.AudioPlayer
//import com.kafka.player.model.PlaybackItem
//import com.kafka.player.model.PlayerConfig
//import com.kafka.player.model.PlayerState
//import com.kafka.user.extensions.getService
//import io.reactivex.schedulers.Schedulers
//
///**
// * @author Vipul Kumar; dated 29/03/19.
// */
//class PlayerService : LifecycleService() {
//
//    inner class ServiceBinder : Binder() {
//        val service: PlayerService
//            get() = this@PlayerService
//    }
//
//    var player: AudioPlayer? = AudioPlayer()
//    private lateinit var notificationManager: NotificationManager
//    private var lastPlayerState: PlayerState = PlayerState.Stopped(false)
//
//    override fun onCreate() {
//        super.onCreate()
//        notificationManager = getService(Context.NOTIFICATION_SERVICE)
//        initPlayer()
//    }
//
//    fun playerState() = player?.lastPlayerState
//
//    fun isPlaying() = lastPlayerState is PlayerState.Playing
//
//    val playerStateObservable = player?.playerStateObservable
//
//    private fun initPlayer() {
//        player?.playerStateObservable?.observeOn(Schedulers.io())
//            ?.subscribe {
//                lastPlayerState = it
//                d("State $it")
//            }
//            ?.disposeOnDestroy(this)
//
//        loadUrl(PlaybackItem(dummyUrl))
//    }
//
//    private fun loadUrl(playbackItem: PlaybackItem) {
//        player?.load(playbackItem, PlayerConfig(""))
////        player?.play()
//    }
//
//    private val binder: IBinder = ServiceBinder()
//
//    override fun onBind(intent: Intent?): IBinder? {
//        super.onBind(intent)
//        return binder
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        intent?.let {
//            showNotification(it)
//            handleIntent(it)
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    private fun handleIntent(intent: Intent?) {
//        intent?.action?.let {
//            when (it) {
//                PlayerCommand.TOGGLE.toString() -> {
////                    togglePlayback()
//                }
//                PlayerCommand.NEXT.toString() -> {
////                    if (playerQueue.hasNext())
////                        playNextSong()
//                }
//                PlayerCommand.PREV.toString() -> {
////                    if (playerQueue.hasPrev())
////                        playPrevSong()
//                }
//                PlayerCommand.STOP.toString() -> {
//                    if(!isPlaying()) {
//                        player?.stop()
//                    }
//                }
//            }
//        }
//    }
//
//    private var notification: Notification? = null
//
//    private fun showNotification(intent: Intent) {
//        notification?.let {
//            if (lastPlayerState is PlayerState.Playing) {
//                startForeground(100, it)
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        release()
//    }
//
//    fun play() {
//        player?.play()
//    }
//
//    fun pause() {
//        player?.pause()
//    }
//
//    fun load(playbackItem: PlaybackItem) {
//        player?.load(playbackItem, PlayerConfig(""))
//    }
//
//    fun play(playbackItem: PlaybackItem) {
//        player?.load(playbackItem, player?.currentPlayerConfig!!)
//        player?.play()
//    }
//
//    fun currentItem() = player?.currentPlaybackItem
//
//    private fun clearNotification() {
//        notificationManager.cancel(100)
//        stopForeground(true)
//    }
//
//    private fun release() {
//        player?.destroy()
//        player = null
//    }
//}
