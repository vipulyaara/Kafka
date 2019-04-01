//package com.kafka.player.service
//
//import android.app.Notification
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.Drawable
//import android.os.Build
//import android.os.Bundle
//import android.os.IBinder
//import android.support.v4.media.session.MediaSessionCompat
//import android.transition.Transition
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.LifecycleService
//import androidx.media.session.MediaButtonReceiver.handleIntent
//import com.google.android.exoplayer2.ExoPlayer
//import com.google.common.eventbus.Subscribe
//import com.kafka.player.R
//import com.kafka.player.com.kafka.player.model.PlaybackItem
//import com.kafka.player.notification.NotificationChannelManager
//import com.kafka.player.notification.NotificationHelper
//import com.kafka.player.notification.NotificationHelper.updateNotification
//import com.kafka.player.queue.PlayerQueueImp
//import jp.wasabeef.glide.transformations.internal.Utils
//
///**
// * @author Vipul Kumar; dated 05/03/19.
// */
//class PlayerService : LifecycleService() {
//
//    var playerQueue = PlayerQueueImp.getInstance()
//    private val playerServiceViewModel = PlayerServiceViewModel()
//
//    private var player: ExoPlayer? = null
//
//    override fun onCreate() {
//        super.onCreate()
//    }
//
//    private fun createPlaySessionId(): String {
//        return System.currentTimeMillis().toString()
//    }
//
//    private fun handleError(streamingErrorCode: String?) {
//        //error
//        player?.stop()
//    }
//
//    private fun initPlayer() {
//        player = ExoPlayer.getInstance(this)
//        player?.addListener(this)
//    }
//
//    private fun onFupReached() {
//        stopPlayer()
//        NotificationHelper.showFUEndNotification(this,
//            object : NotificationChannelManager.OnNotificationUpdateListener {
//                override fun onNotificationUpdate(notification: Notification) {
//                    nm.cancel(NOTIFICATION_ID)
//                    nm.notify(FUP_NOTIFICATION, notification)
//                }
//            })
//    }
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
//    private var notification: Notification? = null
//
//    private fun showNotification(intent: Intent) {
//        val showNotification = intent.getBooleanExtra(SHOW_NOTIFICATION, false)
//        if (showNotification) {
//            notification?.let {
//                if (isPlaying()) {
//                    sNotificationState = PlayerConstants.NotificationState.VISIBLE
//                    startForeground(NOTIFICATION_ID, it)
//                }
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cancelShutdown()
//        purgePlayer()
//        PlayerServiceIntentReceiver.unregister(this)
//        WynkEventBus.unSubscribe(this)
//    }
//
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        super.onTaskRemoved(rootIntent)
//        stopSelf()
//    }
//
//    fun playSong(item: Item) {
//        playerQueue.enqueueSong(item)
//    }
//
//    fun onPlayerStateChanged(player: IPlayer?, playerState: Int, extras: Bundle?) {
////        WynkEventBus.postEvent(PlayerStateEvent(playerState, extras))
//        this.playerState = playerState
//
//        if (isPlaying() || playerState == IPlayer.STATE_PAUSED) {
//            showNotification(playerState)
//        }
//        when (playerState) {
//            IPlayer.STATE_SONG_COMPLETED, IPlayer.STATE_ENDED -> {
//                playNextSong()
//            }
//        }
//    }
//
//    private fun showNotification(playerState: Int) {
//        getCurrentSong()?.let {
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
//    @Subscribe
//    fun onSongChange(event: SongChangeEvent) {
//        event.currentSong?.let { it ->
//            player?.let {
//                if (it.isPlaying)
//                    it.stop()
//            }
//            playNew(it)
//        }
//    }
//
//    private lateinit var mCurrentSongAttributes: CurrentSongAttributes
//
//    private fun playNew(item: Item) {
//        if (Utils.isOnline()) {
//            if (fupReched) {
//                onFupReached()
//                return
//            }
//            if (player == null)
//                initPlayer()
//
//            mCurrentSongAttributes = CurrentSongAttributes()
//            fetchPlaybackResponse(item.id)
//        } else {
//            Utils.showToast(
//
//                this.applicationContext.getString(R.string.no_network)
//            )
//            player?.pause()
//            player?.stop()
//        }
//    }
//
//    fun fetchPlaybackResponse(id: String) {
//        playbacktime = System.currentTimeMillis()
//        playbackTrigger.value = FetchPlaybackResponseTrigger(id)
//    }
//
//    private fun startServiceAsForeground() {
//        val intent = Intent(applicationContext, PlayerService::class.java)
//        intent.putExtra(SHOW_NOTIFICATION, true)
//        ContextCompat.startForegroundService(applicationContext, intent)
//    }
//
//    fun purgePlayer() {
////        sendEvents(IPlayer.STATE_STOPPED, null)
//        release()
//        clearQueue()
//        clearNotification()
//        stopSelf()
//        playSessionId = "NA"
//    }
//
//    private fun release() {
//        player?.let {
//            it.clearListeners()
//            it.release()
//        }
//        player = null
//    }
//
//    private fun scheduleDelayedShutdown() {
//        mDelayedStopHandler.removeCallbacksAndMessages(null)
//        mDelayedStopHandler.sendEmptyMessageDelayed(0, IDLE_DELAY.toLong())
//    }
//
//    private fun cancelShutdown() {
//        mDelayedStopHandler.removeCallbacksAndMessages(null)
//    }
//
//    fun togglePlayback() {
//        var action = util.PlayerConstants.Analytics.PAUSE
//        player?.let {
//            if (!it.isPlaying) {
//                action = util.PlayerConstants.Analytics.PLAY
//            }
//        }
//        // analytics
//
//        if (player == null)
//            initPlayer()
//        player?.let {
//            if (it.isPlaying)
//                pauseSong()
//            else {
//                if (Utils.isOnline()) {
//                    playSong()
//                } else {
//                    Utils.showToast(
//
//                        this.applicationContext.getString(R.string.no_network)
//                    )
//                    it?.pause()
//                    it?.stop()
//                }
//            }
//        }
//    }
//
//    fun enqueue(item: PlaybackItem) {
//        playerQueue.enqueue(item)
//    }
//
//    fun enqueue(item: PlaybackItem, song: PlaybackItem) {
//        playerQueue.enqueue(item, song)
//    }
//
//    fun playNextSong() {
//        playerQueue.next()
//    }
//
//    fun playPrevSong() {
//        playerQueue.prev()
//    }
//
//    fun pauseSong() {
//        player?.pause()
//    }
//
//    fun playSong() {
//        if (playerState == IPlayer.STATE_ERROR || playerState == IPlayer.STATE_STOPPED) {
//            playNew(getCurrentSong()!!)
//        } else {
//            player?.start()
//        }
//    }
//
//    fun clearQueue() {
//        playerQueue.clear()
//    }
//
//    fun currentPlaylist(): PlaybackItem? {
//        return playerQueue.currentPlaylist()
//    }
//
//    fun isPlaying(): Boolean {
//        player?.let {
//            return it.isPlaying
//        }
//        return false
//    }
//
//    fun getCurrentSong(): PlaybackItem? = playerQueue.currentSong()
//}
