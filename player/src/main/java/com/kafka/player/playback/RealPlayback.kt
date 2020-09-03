//package com.kafka.player.playback
//
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.os.Build
//import android.os.Handler
//import androidx.core.net.toUri
//import com.google.android.exoplayer2.C
//import com.google.android.exoplayer2.ExoPlaybackException
//import com.google.android.exoplayer2.Player.EventListener
//import com.google.android.exoplayer2.SimpleExoPlayer
//import com.google.android.exoplayer2.Timeline
//import com.google.android.exoplayer2.database.ExoDatabaseProvider
//import com.google.android.exoplayer2.source.ConcatenatingMediaSource
//import com.google.android.exoplayer2.source.MediaSource
//import com.google.android.exoplayer2.source.MediaSourceEventListener
//import com.google.android.exoplayer2.source.ProgressiveMediaSource
//import com.google.android.exoplayer2.ui.PlayerNotificationManager
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
//import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
//import com.google.android.exoplayer2.upstream.cache.SimpleCache
//import com.google.android.exoplayer2.util.Util
//import com.kafka.data.CustomScope
//import com.kafka.player.playback.model.MediaItem
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.launch
//import timber.log.Timber
//import java.io.File
//import javax.inject.Inject
//import javax.inject.Singleton
//import kotlin.math.pow
//
//@Singleton
//class RealPlayback @Inject constructor(
//    @ApplicationContext private val context: Context
//) : PlayerContract, EventListener,
//    PlayerNotificationManager.NotificationListener, CoroutineScope by CustomScope() {
//
//    private val player: SimpleExoPlayer by lazy {
//        SimpleExoPlayer.Builder(context)
//            .setUseLazyPreparation(true)
//            .build()
//            .apply {
//                addListener(this@RealPlayback)
//            }
//    }
//
//
//    private val playerNotificationManager: PlayerNotificationManager by lazy {
//        createNotificationChannel()
//
//        PlayerNotificationManager(
//            context,
//            CHANNEL_ID,
//            NOTIFICATION_ID,
//            descriptionAdapter,
//            this
//        ).apply {
//            setColorized(true)
//
//        }
//    }
//
//    private val background = CoroutineScope(Dispatchers.IO + Job())
//
//    val currentMediaItem: MediaItem?
//        get() = player.currentTag as? MediaItem
//
//    override suspend fun initPlayer() {
//        playerNotificationManager.setPlayer(player)
//        player.setHandleAudioBecomingNoisy(true)
//        player.setWakeMode(C.WAKE_MODE_NETWORK)
//        if (ExoPlayerCache.simpleCache(context).cacheSpace > 6.0.pow(6.0)) {
//            Timber.d("The cache is too long starting to evict")
//            background.launch { ExoPlayerCache.simpleCache(context).release() }
//        }
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val importance = NotificationManager.IMPORTANCE_LOW
//            val notificationChannel =
//                NotificationChannel(CHANNEL_ID, NOTIFICATION_CHANEL_NAME, importance)
//            notificationChannel.enableVibration(true)
//            notificationChannel.vibrationPattern = longArrayOf(0L)
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
//            notificationManager?.createNotificationChannel(notificationChannel)
//        }
//    }
//
//    private var concatenatedSource = ConcatenatingMediaSource()
//
//
//    override suspend fun addToQueue(
//        mediaItem: MediaItem,
//        playWhenReady: Boolean,
//        clearOldPlayList: Boolean
//    ) = addToQueue(listOf(mediaItem), playWhenReady, clearOldPlayList)
//
//    override suspend fun addToQueue(
//        mediaItems: List<MediaItem>,
//        playWhenReady: Boolean,
//        clearOldPlayList: Boolean
//    ) {
//        if (clearOldPlayList) {
//            concatenatedSource.clear()
//        }
//        player.playWhenReady = playWhenReady
//        addItems(*mediaItems.toTypedArray())
//    }
//
//    private fun addItems(vararg mediaItems: MediaItem) {
//        if (!player.isPlaying)
//            player.prepare(concatenatedSource)
//
//        mediaItems.forEach {
//            concatenatedSource.addMediaSource(getDataSourceFromTrack(it))
//        }
//    }
//
//    /**
//     * Prepare uri and data source to be able to execute
//     * @param mediaItem Track
//     * @return MediaSource
//     */
//    private fun getDataSourceFromTrack(mediaItem: MediaItem): MediaSource {
//        // Produces DataSource instances through which media data is loaded
//
//        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
//            Util.getUserAgent(context, context.applicationInfo.name),
//            null /* listener */,
//            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
//            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
//            true /* allowCrossProtocolRedirects */
//        )
//
//        val dataSourceFactory = DefaultDataSourceFactory(
//            context,
//            null,
//            httpDataSourceFactory
//        )
//
//        val mediaSource =
//            ProgressiveMediaSource.Factory(/*CacheDataSourceFactory(ExoPlayerCache.simpleCache(context), */
//                dataSourceFactory/*)*/
//            ).setTag(mediaItem).createMediaSource(mediaItem.playbackUrl.toUri())
//
//        /***
//         * Check conditions to allow user listen audios
//         */
//        mediaSource.addEventListener(Handler(), object : MediaSourceEventListener {
//            override fun onLoadStarted(
//                windowIndex: Int,
//                mediaPeriodId: MediaSource.MediaPeriodId?,
//                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
//                mediaLoadData: MediaSourceEventListener.MediaLoadData?
//            ) {
//                /***
//                 * Check preconditions to playback audio
//                 */
//                try {
//                    background.launch {
////                        val hasError = callback?.checkPreconditions(mediaItem)
////                        hasError?.let {
////                            callback?.preconditionsPlaybackFailed(it)
////                        }
//                    }
//                } catch (e: Exception) {
//                    Timber.e(e)
//                }
//            }
//        })
//
//        return mediaSource
//    }
//
//
//    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
//        Timber.d("onTimelineChanged")
//    }
//
//    override fun onLoadingChanged(isLoading: Boolean) {
//        Timber.d("onLoadingChanged:$isLoading")
////        callback?.onLoadingChange(isLoading)
//    }
//
//    override fun onPlayerError(error: ExoPlaybackException) {
//        Timber.e(error, "onPlayerError")
//    }
//
//    override fun onNotificationPosted(
//        notificationId: Int,
//        notification: Notification,
//        ongoing: Boolean
//    ) {
////        callback?.onNotificationChanged(notificationId, notification, ongoing)
//    }
//
//    override suspend fun isPlaying(): Boolean = player.isPlaying
//    override suspend fun next() = player.next()
//    override suspend fun hasNext(): Boolean = player.hasNext()
//    override suspend fun clear() = concatenatedSource.clear()
//    override suspend fun isEmpty(): Boolean = concatenatedSource.size == 0
//
//    override suspend fun release() {
//        playerNotificationManager.setPlayer(null)
//        player.release()
//    }
//
//    object ExoPlayerCache {
//        private var cache: SimpleCache? = null
//        fun simpleCache(context: Context): SimpleCache {
//            if (cache == null) {
//
//                val directory = File(context.cacheDir, "media")
//                if (!directory.exists())
//                    directory.mkdir()
//
//                cache = SimpleCache(
//                    directory,
//                    NoOpCacheEvictor(),
//                    ExoDatabaseProvider(context)
//                )
//
//                Timber.d("Initializing cache exoplayer")
//            }
//            return cache!!
//        }
//    }
//
//    companion object {
//        private const val CHANNEL_ID = "1001"
//        private const val NOTIFICATION_CHANEL_NAME = "Notifications"
//        const val NOTIFICATION_ID = 1001
//    }
//}
