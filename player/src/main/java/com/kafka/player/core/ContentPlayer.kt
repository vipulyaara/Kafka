//package com.kafka.player.core
//
//import android.content.Context
//import android.graphics.Point
//import android.net.Uri
//import android.os.Handler
//import android.os.Looper
//import android.text.TextUtils
//import android.util.AttributeSet
//import android.view.Surface
//import android.view.SurfaceView
//import android.view.View
//import android.view.ViewTreeObserver
//import com.google.android.exoplayer2.*
//import com.google.android.exoplayer2.Player
//import com.google.android.exoplayer2.Player.DISCONTINUITY_REASON_PERIOD_TRANSITION
//import com.google.android.exoplayer2.analytics.AnalyticsListener
//import com.google.android.exoplayer2.analytics.DefaultAnalyticsListener
//import com.google.android.exoplayer2.drm.*
//import com.google.android.exoplayer2.source.*
//import com.google.android.exoplayer2.source.dash.DashMediaSource
//import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
//import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory
//import com.google.android.exoplayer2.source.hls.HlsMediaSource
//import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
//import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
//import com.google.android.exoplayer2.text.Cue
//import com.google.android.exoplayer2.text.TextRenderer
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
//import com.google.android.exoplayer2.trackselection.TrackSelection
//import com.google.android.exoplayer2.trackselection.TrackSelectionArray
//import com.google.android.exoplayer2.upstream.*
//import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
//import com.google.android.exoplayer2.upstream.cache.SimpleCache
//import com.google.android.exoplayer2.util.MimeTypes
//import com.google.android.exoplayer2.util.UriUtil
//import com.google.android.exoplayer2.util.Util
//import com.kafka.data.data.config.kodeinInstance
//import com.kafka.data.data.config.logging.Logger
//import com.kafka.player.analytics.PlayerAnalytics
//import com.kafka.player.helper.TrackSelectionHelper
//import com.kafka.player.model.PlaybackItem
//import com.kafka.player.model.PlayerConfig
//import com.kafka.player.model.PlayerException
//import com.kafka.player.model.PlayerSeekInfo
//import com.kafka.player.model.PlayerState
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.async
//import kotlinx.coroutines.launch
//import org.kodein.di.generic.instance
//import timber.log.Timber
//import java.io.File
//import java.io.IOException
//import java.net.CookieHandler
//import java.net.CookieManager
//import java.net.CookiePolicy
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.ArrayList
//import kotlin.collections.LinkedHashMap
//import kotlin.coroutines.CoroutineContext
//
///**
// * @author Vipul Kumar; dated 05/03/19.
// */
//class ContentPlayer @JvmOverloads constructor(
//        context: Context,
//        attrs: AttributeSet? = null,
//        defStyleAttr: Int = 0) : BasePlayer(context, attrs, defStyleAttr),
//    CoroutineScope {
//
//    private val job = Job()
//    private val logger: Logger by kodeinInstance.instance()
//
//    private lateinit var player: SimpleExoPlayer
//    private val trackSelector: DefaultTrackSelector
//    private val videoTrackSelectionFactory: TrackSelection.Factory
//
//    private val bandwidthMeter: DefaultBandwidthMeter
//    private lateinit var loadControl: DefaultLoadControl
//    private val userAgent: String
//    private val mainHandler: Handler
//    private var seekPositionInMs: Long = -1
//    private var trackSelectionHelper: TrackSelectionHelper? = null
//    private val defaultCookieManager: CookieManager = CookieManager()
//    private val shutter: View
//    private val upstreamFactory: DataSource.Factory
//
//    private var currentFormat: Format? = null
//
//    private var pausedDueToContentSwitch: Boolean = false
//
////    private var lastMaxPlayerWidth = -1
////    private var lastMaxPlayerHeight = -1
//
//    private val simpleCache by lazy {
//        val evictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024)
//        SimpleCache(File(context.cacheDir, "media"), evictor)
//    }
//
//    private var playerIdleReasonStop: Boolean = false
//
//    private var currentPlaybackItem: PlaybackItem? = null
//    private var currentPlayerConfig: PlayerConfig? = null
//
//    //used for exoplayer playlist
//    private var nextPlaybackItem: PlaybackItem? = null
//    private var nextPlayerConfig: PlayerConfig? = null
//
//    private lateinit var currentMediaSource: ConcatenatingMediaSource
//
//    private var timeShiftWindowLastEndTime = 0L
//
//    /**
//     * variables used for analytics only
//     */
//    internal val analyticsModel = PlayerAnalyticsModel()
//
//    init {
//        logger.d(this.javaClass.canonicalName + " init called")
//        val view = inflate(context, R.layout.content_player_layout, this)
//        videoSurfaceView = view.findViewById(R.id.videoSurfaceView)
//        shutter = view.findViewById(R.id.shutter)
//        defaultCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
//        mainHandler = Handler(Looper.getMainLooper())
//        userAgent = Util.getUserAgent(context, context.packageName)
//        bandwidthMeter = DefaultBandwidthMeter()
//        videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
//        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
//
//        upstreamFactory = DefaultDataSourceFactory(context, bandwidthMeter,
//                buildDataSourceFactory(bandwidthMeter))
//
//        if (CookieHandler.getDefault() !== defaultCookieManager) {
//            CookieHandler.setDefault(defaultCookieManager)
//        }
//
//        trackSelectionHelper = TrackSelectionHelper(trackSelector, null)
//    }
//
//    override fun load(playbackItem: PlaybackItem, playerConfig: PlayerConfig) {
//        super.load(playbackItem, playerConfig)
//        logger.d(this.javaClass.canonicalName + "load() id called")
//        if (playbackItem.addToPlayList) {
//            nextPlaybackItem = playbackItem
//            nextPlayerConfig = playerConfig
//            loadPlaybackItem(playbackItem.contentUrl, playbackItem.contentType, Math.max(0, playbackItem.resumePointInMilliSeconds), playbackItem.requestCookieProperties, playerConfig, true)
//        } else {
//            val oldConfig = currentPlayerConfig
//            updatePlayerConfig(playerConfig)
//            currentPlaybackItem = playbackItem
//            seekPositionInMs = Math.max(0, playbackItem.resumePointInMilliSeconds)
//            logger.d("seek postion for content is : $seekPositionInMs")
//            if (oldConfig == null || currentPlayerConfig?.drmPlayerConfiguration != oldConfig.drmPlayerConfiguration) {
//                if (playerConfig.drmPlayerConfiguration != null) {
//                    initPlayerWithDRM(playbackItem, playerConfig)
//                } else {
//                    initPlayerWithoutDRM(playbackItem, playerConfig)
//                }
//            } else {
//                //already have a initialized player for this config
//                //TODO check if already playing same content
//                loadPlaybackItem(playbackItem.contentUrl, playbackItem.contentType, seekPositionInMs, playbackItem.requestCookieProperties, playerConfig)
//                updatePlayerState(PlayerState.Idle)
//            }
//        }
//    }
//
//    override fun play() {
//        super.play()
//        if (currentPlaybackItem?.contentType == ContentType.LIVE
//                && System.currentTimeMillis() - pausedAt > 10 * 1000 && pausedAt > 0) {
//            //load timeshift
//            pausedAt = 0
//            seekTo(seekPositionInMs)
//        } else {
//            player.playWhenReady = true
//        }
//    }
//
//    private var pausedAt: Long = 0
//
//    override fun pause() {
//        super.pause()
//        player.playWhenReady = false
//        pausedAt = System.currentTimeMillis()
//    }
//
//    override fun stop() {
//        super.stop()
//        playerIdleReasonStop = true
//        player.stop(true)
//        stopPlayerProgressUpdate()
//    }
//
//    override fun setVolume(volume: Float) {
//        super.setVolume(volume)
//        player.volume = volume
//    }
//
//    override fun setBitrate(bitrate: Int) {
//        super.setBitrate(bitrate)
//        trackSelectionHelper?.setBitRate(bitrate)
//    }
//
//    override fun setBitrates(bitrates: IntArray) {
//        super.setBitrates(bitrates)
//        trackSelectionHelper?.setBitrates(bitrates)
//    }
//
//    override fun seekTo(seekPositionInMs: Long) {
//        super.seekTo(seekPositionInMs)
//        this.seekPositionInMs = seekPositionInMs
//        if (currentPlaybackItem?.contentType == ContentType.LIVE) {
//            inTimeShift = seekPositionInMs != timeShiftWindow
//            if (inTimeShift) {
//                //todo - optimise if new time shift already a part of current playing window
//                loadTimeShift(System.currentTimeMillis() - (timeShiftWindow - seekPositionInMs))
//            } else {
//                loadPlaybackItem(currentPlaybackItem?.contentUrl, currentPlaybackItem?.contentType, 0,
//                        currentPlaybackItem?.requestCookieProperties, currentPlayerConfig)
//            }
//        } else {
//            logger.d("seeking to $seekPositionInMs")
//            player.seekTo(seekPositionInMs)
//        }
//    }
//
//    override fun getCurrentResolution(): Point {
//        return Point(currentFormat?.width ?: -1, currentFormat?.height ?: -1)
//    }
//
//    override fun updatePlayerConfig(playerConfig: PlayerConfig) {
//        currentPlayerConfig = playerConfig
//        if (playerConfig.playerDimension != PlayerDimension.DIMENSION_AUTO) {
//            adjustPlayerDimension(playerConfig.playerDimension)
//        }
//    }
//
//    private fun formatTime(timeInMs: Long, timeFormat: String?, timeZone: String?): String {
//        val df = SimpleDateFormat(timeFormat, Locale.getDefault())
//        df.timeZone = TimeZone.getTimeZone(timeZone)
//        val date = Date(timeInMs)
//        return df.format(date)
//    }
//
//    private fun retryPlayback(startFromBeginning: Boolean) {
//        var seekPosition = seekPositionInMs
//        if (startFromBeginning) {
//            seekPosition = 0
//        }
////        if (isActionValid(BasePlayer.PlayerAction.STOP)) {
////            stop()
////        }
//        val playbackUrl =/* if (isInTimeShiftMode()) currentPlaybackItem?.timeShiftUrl else*/ currentPlaybackItem?.contentUrl
//        loadPlaybackItem(playbackUrl, currentPlaybackItem?.contentType, seekPosition,
//                currentPlaybackItem?.requestCookieProperties, currentPlayerConfig,
//                isRetry = true)
//    }
//
//    override fun destroy() {
//        super.destroy()
//        if (isActionValid(PlayerAction.STOP)) {
//            stop()
//        }
//        if (::player.isInitialized) {
//            player.release()
//        }
//        currentPlaybackItem = null
//        currentPlayerConfig = null
////        simpleCache.release()
//        job.cancel()
//    }
//
//    private fun initPlayerWithoutDRM(playbackItem: PlaybackItem, playerConfig: PlayerConfig) {
//        logger.d(this.javaClass.canonicalName + "initPlayerWithoutDRM()")
//        initLoadControl(playbackItem)
//        player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context), trackSelector, loadControl)
//        player.addAnalyticsListener(PlayerEventListener())
//        videoSurfaceView.requestFocus()
//        player.setVideoSurfaceView(videoSurfaceView)
//        player.addListener(playerEventListener)
//        loadPlaybackItem(playbackItem.contentUrl, playbackItem.contentType, playbackItem.resumePointInMilliSeconds, playbackItem.requestCookieProperties, playerConfig)
//        updatePlayerState(PlayerState.Idle)
//    }
//
//    private fun initLoadControl(playbackItem: PlaybackItem) {
//        loadControl = if (playbackItem.contentType == ContentType.LIVE) {
//            DefaultLoadControl.Builder()
//                    .setBufferDurationsMs(5000,
//                            DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
//                            2000,
//                            2000)
//                    .createDefaultLoadControl()
//        } else {
//            DefaultLoadControl.Builder()
//                    .setBufferDurationsMs(DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
//                            if (ATVPlayer.playerAuthentication.deviceType == DeviceType.DEVICE_PHONE.type) DefaultLoadControl.DEFAULT_MAX_BUFFER_MS else PlayerConstants.MAX_BUFFER_DURATION_FOR_WIFI_DEVICES,
//                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
//                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
//                    .createDefaultLoadControl()
//        }
//    }
//
//    private fun loadPlaybackItem(playUrl: String?, contentType: ContentType?,
//                                 seekPosition: Long?,
//                                 requestCookieProperties: HashMap<String, String>?, playerConfig: PlayerConfig?, addToPlayList: Boolean = false, subtitleUrls: HashMap<String, String>? = null, isRetry: Boolean = false) {
//
//        logger.d(this.javaClass.canonicalName + "loadPlaybackItem() $playUrl $contentType $seekPosition $playerConfig ${subtitleUrls.toString()}")
////                logger.d("loading playback item -> $playUrl")
////                val uri = Uri.parse(playUrl)
//        val mediaSource = mergeMediaSources(playUrl, subtitleUrls)
//
//        val mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo()
//        if (addToPlayList) {
//            logger.d("adding to existing playlist")
//            if (currentMediaSource.size == 2) {
//                //keep only the current and the next one
//                currentMediaSource.removeMediaSource(0)
//            }
//            currentMediaSource.addMediaSource(mediaSource)
//        } else {
//            logger.d(this.javaClass.canonicalName + "loadPlaybackItem() concatenatingMediaSource")
//            currentMediaSource = ConcatenatingMediaSource()
//            currentMediaSource.addMediaSource(mediaSource)
//            analyticsModel.videoPrepareStartTime = System.currentTimeMillis()
//
//            if (contentType == ContentType.LIVE) {
//                UriUtil.isSegmentUriInterception = playerConfig?.rrsSwitch ?: false
//            }
//            UriUtil.requestCookieProperties = requestCookieProperties
//            if (!isRetry) {
//                shutter.visibility = View.VISIBLE
//            }
//            player.prepare(currentMediaSource, true, true)
//            logger.d("seeking to ${seekPosition ?: 0}")
//            player.seekTo(seekPosition ?: 0)
//            player.addVideoDebugListener(object : VideoRendererEventListenerAdapter() {
//                override fun onRenderedFirstFrame(surface: Surface?) {
//                    shutter.visibility = View.GONE
//                    player.addTextOutput(ComponentListener())
//                    if (firstFrameRender.value == null) {
//                        firstFrameRender.value = System.currentTimeMillis()
//                    }
//                    logger.d("rendering first frame ${System.currentTimeMillis()}")
//                }
//            })
//        }
//
//    }
//
//    inner class ComponentListener : TextRenderer.Output {
//        override fun onCues(cues: MutableList<Cue>?) {
//            if (cues?.size ?: 0 > 0) {
////                subtitle.text = cues[0].text
//                videoSubtitle.value = cues!![0].text.toString()
//                logger.d("onCues ${cues[0].text}")
//            }
//        }
//    }
//
//    private fun mergeMediaSources(playUrl: String?, subtitleUrls: HashMap<String, String>?): MediaSource {
//        logger.d(this.javaClass.canonicalName + " merging media source with subtitles urls ${subtitleUrls.toString()}")
//        val listSources = ArrayList<MediaSource>()
//
//        val mediaSourceContent = buildMediaSource(Uri.parse(playUrl))
//        listSources.add(mediaSourceContent)
//
//        subtitleUrls?.let {
//            var subtitleLang = ArrayList(subtitleUrls?.keys)
//            for (i in subtitleLang) {
//                lateinit var subtitleFormat: Format
//                if (getExtension(subtitleUrls?.get(i)).equals("vtt", true)) {
//
//                    subtitleFormat = Format.createTextSampleFormat(
//                            null, // An identifier for the track. May be null.
//                            MimeTypes.TEXT_VTT, // The mime type. Must be set correctly.
//                            C.SELECTION_FLAG_DEFAULT, // Selection flags for the track.
//                            "$i")
//                } else if (getExtension(subtitleUrls?.get(i)).equals("srt", true)) {
//                    subtitleFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, C.SELECTION_FLAG_DEFAULT, "$i")
//                }
//                listSources.add(SingleSampleMediaSource(Uri.parse(subtitleUrls?.get(i)), upstreamFactory, subtitleFormat, C.TIME_UNSET))
//            }
//        }
//
//        val mediaSources = arrayOfNulls<MediaSource>(listSources.size)
//        for (i in listSources.indices) {
//            mediaSources[i] = listSources[i]
//        }
//        return MergingMediaSource(*mediaSources)
//    }
//
//    private fun getExtension(url: String?): String? {
//        if (TextUtils.isEmpty(url)) {
//            return url
//        }
//        val aUrl = url!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//        return aUrl[aUrl.size - 1]
//    }
//
//    override fun onDrmLicenseFetchingError(playerError: PlayerError, domain: String) {
//        updatePlayerState(PlayerState.Error(playerError.errorCode, playerError.errorMsg
//                ?: "", playerError.errorReason?.name))
//    }
//
//    override fun getAvailableFormats(): List<VideoFormat> {
//        val bitRates = ArrayList<VideoFormat>()
//        trackSelectionHelper?.availableFormats?.forEach {
//            bitRates.add(VideoFormat(it.bitrate, it.width, it.height, it.frameRate))
//        }
//        return bitRates
//    }
//
//    override fun getCurrentBitrate(): Int {
//        return currentFormat?.bitrate ?: -1
//    }
//
//    override fun getCurrentBandwidth(): Long {
//        return bandwidthMeter.bitrateEstimate
//    }
//
//    private fun buildDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter): HttpDataSource.Factory {
//        return DefaultHttpDataSourceFactory(userAgent, bandwidthMeter, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
//                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true)
//    }
//
//
//    private fun buildMediaSource(uri: Uri): MediaSource {
////        val dataSourceFactory = if (currentPlaybackItem?.contentType == ContentType.LIVE) upstreamFactory else cachedDataSourceFactory
//        val dataSourceFactory = upstreamFactory
//        val type = Util.inferContentType(uri)
//        val mediaSource = when (type) {
//            C.TYPE_SS -> {
//                SsMediaSource.Factory(DefaultSsChunkSource.Factory(dataSourceFactory), dataSourceFactory).createMediaSource(uri)
//            }
//            C.TYPE_DASH -> {
//                DashMediaSource.Factory(DefaultDashChunkSource.Factory(dataSourceFactory), dataSourceFactory).createMediaSource(uri)
//            }
//            C.TYPE_HLS -> {
//                HlsMediaSource.Factory(DefaultHlsDataSourceFactory(dataSourceFactory))
//                        .createMediaSource(uri)
//            }
//            C.TYPE_OTHER -> {
//                ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
//            }
//            else -> {
//                throw PlayerException("Unsupported media source type: $type")
//            }
//        }
//        mediaSource.addEventListener(mainHandler, eventListener)
//        return mediaSource
//    }
//
//    /**
//     * Listener objects
//     */
//    private val eventListener = object : MediaSourceEventListenerAdapter() {
//
//        override fun onLoadCompleted(windowIndex: Int, mediaPeriodId: MediaSource.MediaPeriodId?, loadEventInfo: MediaSourceEventListener.LoadEventInfo?, mediaLoadData: MediaSourceEventListener.MediaLoadData) {
//            super.onLoadCompleted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData)
//            val loadTime = loadEventInfo?.loadDurationMs ?: 0
//            when (mediaLoadData.dataType) {
//                C.DATA_TYPE_MANIFEST -> {
//                    logger.d("Manifest/Index fetched in $loadTime milliseconds")
//                }
//                C.DATA_TYPE_MEDIA -> {
//                    currentFormat = mediaLoadData.trackFormat
//                }
//
//            }
//            logger.v("loaded - > ${mediaLoadData.dataType} for url -> ${loadEventInfo?.dataSpec?.uri}")
//        }
//
//    }
//
//    private val playerEventListener = object : Player.DefaultEventListener() {
//
//        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
//            logger.v("timeline changed reason : $reason")
//            updatePlayerSeekInfo()
//        }
//
//        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//            when (playbackState) {
//                com.google.android.exoplayer2.Player.STATE_BUFFERING -> {
//                    if (getPlayerState() is PlayerState.Stopped) {
//                        return
//                    }
//                    updatePlayerState(PlayerState.Buffering(playWhenReady))
//                }
//
//                com.google.android.exoplayer2.Player.STATE_READY -> {
//                    updatePlayerSeekInfo()
//                    if (!analyticsModel.isFirstTimePlayed) {
////                        initTrackSelectionHelper()
//                        analyticsModel.isFirstTimePlayed = true
//                    }/* else if (trackSelectionHelper?.availableBitRates?.size == 0) {
//                        initTrackSelectionHelper()
//                    }*/
//                    if (playWhenReady) {
//                        updatePlayerState(PlayerState.Playing(-1))
//                    } else {
//                        if (pausedDueToContentSwitch) {
//                            pausedDueToContentSwitch = false
//                        } else {
//                            updatePlayerState(PlayerState.Paused)
//                        }
//                    }
//                }
//                com.google.android.exoplayer2.Player.STATE_ENDED -> if (player.currentPosition >= player.duration && player.duration > 0) {
//                    stopPlayerProgressUpdate()
//                    //TODO ANalytics
////                    sendPlayStopEventIfNeeded(true)
//                    updatePlayerState(PlayerState.Finished(-1))
//                }
//                Player.STATE_IDLE -> {
//                    shutter.visibility = View.GONE
//                    if (playerIdleReasonStop) {
//                        //idle due to player.stop()
//                        stopPlayerProgressUpdate()
//                        updatePlayerState(PlayerState.Stopped(true))
//                        playerIdleReasonStop = false
//                    }
//                }
//            }
//
//        }
//
//        override fun onPlayerError(error: ExoPlaybackException) {
//            super.onPlayerError(error)
//            stopPlayerProgressUpdate()
//            val playerError = PlayerError(error, getPlayerState())
//            logger.e("playback error: $playerError")
//            if (playerError.isRecoverable) {
//                try {
//                    retryPlayback(true)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            } else {
////                updatePlayerState(PlayerState.Error(playerError.errorCode, playerError.errorMsg, playerError.errorReason?.name))
//                updatePlayerState(PlayerState.Error
//                (playerError.errorCode,
//                        playerError.errorMsg ?: "Unknown error while playback",
//                        "Can not play content, Please try again",
//                        true,
//                        ServerErrorDetails(playerError.errorCode,
//                                null,
//                                playerError.errorMsg,
//                                playerError.errorReason?.name),
//                        stackTrace = playerError.stackTrace))
//                //set null because we want to send play_start event again on retry after error
//                firstFrameRender.value = null
//            }
//        }
//
//        override fun onPositionDiscontinuity(reason: Int) {
//            super.onPositionDiscontinuity(reason)
//            if (player.currentWindowIndex > 0 && reason == DISCONTINUITY_REASON_PERIOD_TRANSITION) {
//                if (isInTimeShiftMode()) {
//                    //seek to 0 for 2nd time onward shift url load. 1st load is handled at onTracksChanged()
//                    logger.i("seeking to 0 because onPositionDiscontinuity with reason DISCONTINUITY_REASON_PERIOD_TRANSITION")
//                    player.seekTo(player.currentWindowIndex, 0)
//                } else {
//                    //content switched due to playlist
//                    logger.d("VISION Starting new content")
//                    pausedDueToContentSwitch = true
//                    player.playWhenReady = false
//                    logger.i("started playing next content in playlist")
//                    currentPlaybackItem = nextPlaybackItem
//                    currentPlayerConfig?.let {
//                        updatePlayerConfig(it)
//                    }
//                    seekPositionInMs = Math.max(0, currentPlaybackItem?.resumePointInMilliSeconds
//                            ?: 0)
//                    updatePlayerState(PlayerState.Finished(-1))
////                    updatePlayerState(PlayerState.Idle)
//                }
//            }
//        }
//
//        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
//            super.onTracksChanged(trackGroups, trackSelections)
//            //manifest loaded
//            val current = player.videoFormat
//            logger.d("tracks changed $current")
//            analyticsModel.hasManifestBeenFetched = true
////            analyticsModel.manifestFetchTime = loadTime
//            if (isInTimeShiftMode() && isTimeShiftManual) {
//                //seek to 0 for 1st time shift url load. subsequent loads are handled at onPositionDiscontinuity()
//                logger.d("seeking content to start from 0")
//                player.seekTo(0)
//            }
//        }
//    }
//
//    private val updateProgressAction = Runnable { updatePlayerSeekInfo() }
//
//    private fun updatePlayerSeekInfo() {
//        var currentPosition = player.currentPosition
//        var duration = if (player.duration < 0) 0 else player.duration
//        var bufferedPosition = player.bufferedPosition
//        logger.d("player progress -> current position : ${player.currentPosition}, duration -> ${player.duration}")
//        if (currentPlaybackItem?.contentType == ContentType.LIVE) {
//            if (player.playWhenReady || pausedAt == 0L) {
//                currentPosition = if (isInTimeShiftMode()) {
//                    val percentageOfContentPlayed = Math.min(Math.max(player.currentPosition, 0) / (player.duration * 1.0), 100.0)
//                    val contentLeftToBePlayedInMs = player.duration - player.currentPosition
//                    logger.d("percentage of content played - $percentageOfContentPlayed")
//                    if ((percentageOfContentPlayed >= timeShiftBufferWindowPercentage) && !isTimeShiftUrlLoaded) {
//                        isTimeShiftUrlLoaded = true
//                        logger.d("${percentageOfContentPlayed * 100}% content played. time left = ${(contentLeftToBePlayedInMs) / 1000.0} secs. Loading new url")
//                        loadTimeShift(timeShiftWindowLastEndTime, false)
//                    } else if (percentageOfContentPlayed < timeShiftBufferWindowPercentage) {
//                        //last time shift content already added to media source. Reset state to get ready to accept next time shift url
//                        isTimeShiftUrlLoaded = false
//                    }
//                    seekPositionInMs
//                } else {
//                    timeShiftWindow
//                }
//            } else {
//                currentPosition = if (isInTimeShiftMode()) {
//                    seekPositionInMs
//                } else {
//                    timeShiftWindow - (System.currentTimeMillis() - pausedAt)
//                }
//            }
//            bufferedPosition = timeShiftWindow
//            duration = timeShiftWindow
//        }
//        seekPositionInMs = currentPosition
//        updatePlayerSeekInfo(PlayerSeekInfo(currentPosition, duration, bufferedPosition))
//        stopPlayerProgressUpdate()
//        if (isContentPlaying()) {
//            mainHandler.postDelayed(updateProgressAction, seekUpdateInterval)
//        }
//    }
//
//    private fun initTrackSelectionHelper() {
//        if (trackSelector.currentMappedTrackInfo == null) {
//            return
//        }
//        val rendererCount = trackSelector.currentMappedTrackInfo?.rendererCount ?: 0
//        for (i in 0 until rendererCount) {
//            val trackGroups = trackSelector.currentMappedTrackInfo?.getTrackGroups(i)
//            if (trackGroups?.length != 0) {
//                if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO) {
//                    trackSelectionHelper?.setAvailableBitRates(i, trackGroups)
//                    break
//                }
//            }
//        }
//    }
//
//    private fun stopPlayerProgressUpdate() {
//        mainHandler.removeCallbacks(updateProgressAction)
//    }
//
//
//    private fun getTimeFromSecond(milliseconds: Long, timeFormat: String?, timeZone: String?): String {
//        val df = SimpleDateFormat(timeFormat, Locale.US)
//        val calendar = GregorianCalendar()
//        calendar.timeZone = TimeZone.getTimeZone(timeZone)
//        calendar.timeInMillis = milliseconds
//        calendar.add(Calendar.HOUR_OF_DAY, -5)
//        calendar.add(Calendar.MINUTE, -30)
//        logger.d("time : ${calendar.time}")
//        return df.format(calendar.time)
//    }
//
//    private fun getTimefromSecondInLocalTime(milliseconds: Long, hour: Int, min: Int, seconds: Int, timeFormat: String?, timeZone: String?): String {
//        val df = SimpleDateFormat(timeFormat, Locale.US)
//        val calendar = GregorianCalendar()
//        calendar.timeZone = TimeZone.getTimeZone(timeZone)
//        calendar.timeInMillis = milliseconds
//        calendar.add(Calendar.HOUR_OF_DAY, hour)
//        calendar.add(Calendar.MINUTE, min)
//        calendar.add(Calendar.SECOND, seconds)
//        logger.d("time : ${calendar.time}")
//        return df.format(calendar.time)
//    }
//
//    fun getTimefromSecondISO(second: Long, hour: Int, min: Int, seconds: Int, timeFormat: String?, timeZone: String?): Long {
//        val df = SimpleDateFormat(timeFormat, Locale.UK)
//        val calendar = GregorianCalendar()
//        calendar.timeZone = TimeZone.getTimeZone(timeZone)
//        calendar.timeInMillis = second * 1000
//        logger.d(" current UTC Time: " + calendar.time)
//        calendar.add(Calendar.HOUR_OF_DAY, hour)
//        calendar.add(Calendar.MINUTE, min)
//        calendar.add(Calendar.SECOND, seconds)
//        logger.d(" current after GMT Time: " + calendar.time)
//        logger.d(" current appstreet   " + df.format(calendar.time))
//        return calendar.timeInMillis / 1000L
//    }
//
//    fun getTimefromSecondISO(milliSecond: Long, timeFormat: String?, timeZone: String?): Long {
//        val df = SimpleDateFormat(timeFormat, Locale.UK)
//        val calendar = GregorianCalendar()
//        calendar.timeZone = TimeZone.getTimeZone(timeZone)
//        calendar.timeInMillis = milliSecond
//        logger.d(" current UTC Time: " + calendar.time)
//        logger.d(" current after GMT Time: " + calendar.time)
//        logger.d(" current appstreet   " + df.format(calendar.time))
//        return calendar.timeInMillis / 1000L
//    }
//
//    private var subtitles: LinkedHashMap<String, TrackIndexes> = LinkedHashMap()
//    private var trackGroups: TrackGroupArray? = null
//    private var isDisabled: Boolean = false
//    private var override: DefaultTrackSelector.SelectionOverride? = null
//    override fun getSubtitleList(): ArrayList<String>? {
//        trackGroups = trackSelector.currentMappedTrackInfo?.getTrackGroups(2)
//        val parameters = trackSelector.parameters
//        isDisabled = parameters.getRendererDisabled(2)
//        override = parameters.getSelectionOverride(2, trackGroups)
//        trackGroups?.let {
//            for (groupIndex in 0 until trackGroups!!.length step 1) {
//                val group = trackGroups!!.get(groupIndex)
//                for (trackIndex in 0 until group!!.length step 1) {
//                    group?.getFormat(trackIndex)?.language?.let {
//                        subtitles[group.getFormat(trackIndex).language.languageCode2DisplayName()] = TrackIndexes(group.getFormat(trackIndex).language!!, groupIndex, trackIndex)
//                    }
//                }
//            }
//            return ArrayList(subtitles.keys)
//        }
//        return null
//    }
//
//    override fun setSubtitle(subtitle: String) {
//        val indexes = subtitles[subtitle]
//        if (indexes != null) {
//            isDisabled = false
//            override = DefaultTrackSelector.SelectionOverride(indexes.groupIndex, indexes.trackIndex)
//        } else {
//            isDisabled = true
//        }
//        applySelection()
//    }
//
//    private fun applySelection() {
//        val parametersBuilder = trackSelector.buildUponParameters()
//        parametersBuilder.setRendererDisabled(2, isDisabled)
//        if (override != null) {
//            parametersBuilder.setSelectionOverride(2, trackGroups, override)
//        } else {
//            parametersBuilder.clearSelectionOverrides(2)
//        }
//        trackSelector.setParameters(parametersBuilder)
//    }
//
//    override val coroutineContext: CoroutineContext
//        get() = job + Dispatchers.Main
//
//    inner class PlayerEventListener : DefaultAnalyticsListener() {
//
//        override fun onLoadCompleted(eventTime: AnalyticsListener.EventTime, loadEventInfo: MediaSourceEventListener.LoadEventInfo, mediaLoadData: MediaSourceEventListener.MediaLoadData) {
//            super.onLoadCompleted(eventTime, loadEventInfo, mediaLoadData)
//            if (mediaLoadData.dataType == C.DATA_TYPE_MEDIA) {
//                logger.d("loaded segment uri : ${loadEventInfo.dataSpec.uri}")
//                PlayerAnalytics.onSegmentDownloaded(loadEventInfo, mediaLoadData)
//            }
//        }
//
//        override fun onTracksChanged(eventTime: AnalyticsListener.EventTime?, trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
//            super.onTracksChanged(eventTime, trackGroups, trackSelections)
//            initTrackSelectionHelper()
//        }
//    }
//}
