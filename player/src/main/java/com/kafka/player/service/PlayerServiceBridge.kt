//package com.kafka.player.service
//
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.ServiceConnection
//import android.os.IBinder
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.kafka.data.data.annotations.UseSingleton
//import com.kafka.player.com.kafka.player.model.PlaybackItem
//import timber.log.Timber
//
///**
// * Created by Prashant Kumar on 10/9/18.
// */
//
//@UseSingleton
//class PlayerServiceBridge : ServiceBridge {
//
//    companion object {
//        fun getInstance() = PlayerServiceBridge()
//    }
//
//    private var playerService: PlayerService? = null
//
//    val LOG_TAG = "PLAYER_SERVICE_BRIDGE"
//    private var isUnbinded = false
//
//    val playingContentLD: LiveData<MiniPlayerItemModel?> = MutableLiveData()
//
//    fun bindService(context: Context) {
//        val intent = Intent(context, PlayerService::class.java)
//        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
//        isUnbinded = false
//    }
//
//    fun unbindService(context: Context) {
//        if (isUnbinded) {
//            return
//        }
//        context.unbindService(serviceConnection)
//        isUnbinded = true
//    }
//
//    fun isServiceConnected(): Boolean {
//        return playerService != null
//    }
//
//    private val serviceConnection = object : ServiceConnection {
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            Timber.i("%s :: Player Service Disconnected", LOG_TAG)
//            WynkEventBus.postEvent(PlayerServiceEvent(false))
//        }
//
//        override fun onServiceConnected(name: ComponentName, pBinder: IBinder) {
//            Timber.i("%s :: Player Service Connected", LOG_TAG)
//            val binder = pBinder as PlayerService.ServiceBinder
//            playerService = binder.service
//            WynkEventBus.postEvent(PlayerServiceEvent(true))
//        }
//    }
//
//    override fun currentSong(): PlaybackItem? {
//        playerService?.let {
//            return it.getCurrentSong()
//        }
//        return null
//    }
//
//    override fun enqueue(item: PlaybackItem) {
//        playerService?.enqueue(item)
//    }
//
//    override fun enqueue(item: PlaybackItem, song: PlaybackItem) {
//        playerService?.enqueue(item, song)
//    }
//
//    override fun playSong(item: PlaybackItem) {
//        playerService?.playSong(item)
//    }
//
//    override fun playNextSong() {
//        playerService?.playNextSong()
//    }
//
//    override fun playPrevSong() {
//        playerService?.playPrevSong()
//    }
//
//    override fun clearQueue() {
//        playerService?.clearQueue()
//    }
//
//    fun isPlayerEnabled(): Boolean {
//        return playerService != null
//    }
//
//    override fun togglePlayback() {
//        playerService?.togglePlayback()
//    }
//
//    fun isQueueActive(): Boolean {
//        playerService?.let {
//            return it.playerQueue.queueItems()?.isEmpty() ?: false
//        }
//        return false
//    }
//
//    override fun currentPlaylist(): Item? {
//        playerService?.let {
//            return it.currentPlaylist()
//        }
//        return null
//    }
//
//    override fun pause() {
//        playerService?.pauseSong()
//    }
//
//    override fun seekToPosition(progress: Int) {
//        playerService?.seekToPosition(progress)
//    }
//
//    override fun isPlaying(): Boolean {
//        playerService?.let {
//            return it.isPlaying()
//        }
//        return false
//    }
//
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
//
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
//    fun setSourceName(sourceName: String) {
//        playerService?.setSourceName(sourceName)
//    }
//
//    fun getSourceName(): String? {
//        return playerService?.getSourceName()
//    }
//
//    fun clearFUPNotification() {
//        playerService?.let {
//            it.clearFUPNotification()
//        }
//    }
//
//    var contentType: ContentType = ContentType.PLAYLIST
//
//    var contentId: String = ""
//
//    var packageId: String = ""
//}
