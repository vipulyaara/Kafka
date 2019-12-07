package com.kafka.player.core

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.DISCONTINUITY_REASON_PERIOD_TRANSITION
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.analytics.DefaultAnalyticsListener
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.kafka.data.data.config.logging.Logger
import com.kafka.player.analytics.PlayerAnalyticsModel
import com.kafka.player.helper.TrackSelectionHelper
import com.kafka.player.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.coroutines.CoroutineContext

/**
 * @author Vipul Kumar; dated 05/03/19.
 */
class AudioPlayer(
    private val context: Context, private val logger: Logger
) : BasePlayer(), CoroutineScope {

    private val maxBufferDurationForWifiDevices = 1000 * 60 * 10
    private val loggerTag by lazy { this.javaClass.canonicalName }

    private val job = Job()

    private lateinit var player: SimpleExoPlayer
    private val trackSelector: DefaultTrackSelector
    private val videoTrackSelectionFactory: TrackSelection.Factory

    private val bandwidthMeter: DefaultBandwidthMeter
    private lateinit var loadControl: DefaultLoadControl
    private val userAgent: String
    private val mainHandler: Handler
    private var seekPositionInMs: Long = -1
    private var trackSelectionHelper: TrackSelectionHelper? = null
    private val defaultCookieManager: CookieManager = CookieManager()
    private val upstreamFactory: DataSource.Factory

    private var pausedDueToContentSwitch: Boolean = false

    private var playerIdleReasonStop: Boolean = false

    var currentPlaybackItem: PlaybackItem? = null
    var currentPlayerConfig: PlayerConfig? = null

    //used for exoPlayer playlist
    private var nextPlaybackItem: PlaybackItem? = null
    private var nextPlayerConfig: PlayerConfig? = null

    private lateinit var currentMediaSource: ConcatenatingMediaSource

    internal val analyticsModel = PlayerAnalyticsModel()

    init {
        defaultCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        mainHandler = Handler(Looper.getMainLooper())
        userAgent = Util.getUserAgent(context, context.packageName)
        bandwidthMeter = DefaultBandwidthMeter()
        videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        upstreamFactory = DefaultDataSourceFactory(
            context, bandwidthMeter,
            buildDataSourceFactory(bandwidthMeter)
        )

        if (CookieHandler.getDefault() !== defaultCookieManager) {
            CookieHandler.setDefault(defaultCookieManager)
        }

        trackSelectionHelper = TrackSelectionHelper(trackSelector, null)
    }

    override fun load(playbackItem: PlaybackItem, playerConfig: PlayerConfig) {
        super.load(playbackItem, playerConfig)
        if (playbackItem.addToPlayList) {
            nextPlaybackItem = playbackItem
            nextPlayerConfig = playerConfig
            loadPlaybackItem(
                playbackItem.contentUrl,
                Math.max(0, playbackItem.resumePointInMilliSeconds),
                playerConfig,
                true
            )
        } else {
            val oldConfig = currentPlayerConfig
            updatePlayerConfig(playerConfig)
            currentPlaybackItem = playbackItem
            seekPositionInMs = Math.max(0, playbackItem.resumePointInMilliSeconds)
            logger.d("seek position for content is : $seekPositionInMs")
            if (oldConfig == null) {
                initPlayerWithoutDRM(playbackItem, playerConfig)
            } else {
                //already have a initialized player for this config
                //TODO check if already playing same content
                loadPlaybackItem(
                    playbackItem.contentUrl,
                    seekPositionInMs,
                    playerConfig
                )
                updatePlayerState(PlayerState.Idle)
            }
        }
    }

    override fun play() {
        super.play()
        player.playWhenReady = true
    }

    private var pausedAt: Long = 0

    override fun pause() {
        super.pause()
        player.playWhenReady = false
        pausedAt = System.currentTimeMillis()
    }

    override fun stop() {
        super.stop()
        playerIdleReasonStop = true
        player.stop(true)
        stopPlayerProgressUpdate()
    }

    override fun setVolume(volume: Float) {
        super.setVolume(volume)
        player.volume = volume
    }

    override fun seekTo(seekPositionInMs: Long) {
        super.seekTo(seekPositionInMs)
        this.seekPositionInMs = seekPositionInMs

        logger.d("seeking to $seekPositionInMs")
        player.seekTo(seekPositionInMs)
    }

    override fun updatePlayerConfig(playerConfig: PlayerConfig) {
        currentPlayerConfig = playerConfig
    }

    private fun retryPlayback(startFromBeginning: Boolean) {
        var seekPosition = seekPositionInMs
        if (startFromBeginning) {
            seekPosition = 0
        }
//        if (isActionValid(BasePlayer.PlayerAction.STOP)) {
//            stop()
//        }
        val playbackUrl =/* if (isInTimeShiftMode()) currentPlaybackItem?.timeShiftUrl else*/
            currentPlaybackItem?.contentUrl
        loadPlaybackItem(playbackUrl, seekPosition, currentPlayerConfig)
    }

    override fun destroy() {
        super.destroy()
        if (isActionValid(PlayerAction.STOP, getPlayerState())) {
            stop()
        }
        if (::player.isInitialized) {
            player.release()
        }
        currentPlaybackItem = null
        currentPlayerConfig = null
        job.cancel()
    }

    private fun initPlayerWithoutDRM(playbackItem: PlaybackItem, playerConfig: PlayerConfig) {
        logger.d("$loggerTag initPlayerWithoutDRM()")
        initLoadControl(playbackItem)
        player = ExoPlayerFactory.newSimpleInstance(
            context,
            DefaultRenderersFactory(context),
            trackSelector,
            loadControl
        )
        player.addAnalyticsListener(PlayerEventListener())
        player.addListener(playerEventListener)
        loadPlaybackItem(
            playbackItem.contentUrl,
            playbackItem.resumePointInMilliSeconds,
            playerConfig
        )
        updatePlayerState(PlayerState.Idle)
    }

    private fun initLoadControl(playbackItem: PlaybackItem) {
        loadControl =
            DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                    maxBufferDurationForWifiDevices,
                    DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                    DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
                )
                .createDefaultLoadControl()
    }

    private fun loadPlaybackItem(
        playUrl: String?,
        seekPosition: Long?,
        playerConfig: PlayerConfig?,
        addToPlayList: Boolean = false
    ) {
        logger.d("$loggerTag loadPlaybackItem() $playUrl $seekPosition $playerConfig")
        val mediaSource = mergeMediaSources(playUrl)

        if (addToPlayList) {
            logger.d("adding to existing playlist")
            if (currentMediaSource.size == 2) {
                //keep only the current and the next one
                currentMediaSource.removeMediaSource(0)
            }
            currentMediaSource.addMediaSource(mediaSource)
        } else {
            logger.d("$loggerTag loadPlaybackItem() concatenatingMediaSource")
            currentMediaSource = ConcatenatingMediaSource()
            currentMediaSource.addMediaSource(mediaSource)
            analyticsModel.videoPrepareStartTime = System.currentTimeMillis()

//            UriUtil.requestCookieProperties = requestCookieProperties

            player.prepare(currentMediaSource, true, true)
            logger.d("seeking to ${seekPosition ?: 0}")
            player.seekTo(seekPosition ?: 0)
        }
    }

    private fun mergeMediaSources(
        playUrl: String?
    ): MediaSource {
        val listSources = ArrayList<MediaSource>()

        val mediaSourceContent = buildMediaSource(Uri.parse(playUrl))
        listSources.add(mediaSourceContent)

        val mediaSources = arrayOfNulls<MediaSource>(listSources.size)
        for (i in listSources.indices) {
            mediaSources[i] = listSources[i]
        }
        return MergingMediaSource(*mediaSources)
    }

    private fun buildDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(
            userAgent, bandwidthMeter, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true
        )
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = upstreamFactory
        val type = Util.inferContentType(uri)
        val mediaSource = when (type) {
            C.TYPE_SS -> {
                SsMediaSource.Factory(
                    DefaultSsChunkSource.Factory(dataSourceFactory),
                    dataSourceFactory
                ).createMediaSource(uri)
            }
            C.TYPE_DASH -> {
                DashMediaSource.Factory(
                    DefaultDashChunkSource.Factory(dataSourceFactory),
                    dataSourceFactory
                ).createMediaSource(uri)
            }
            C.TYPE_HLS -> {
                HlsMediaSource.Factory(DefaultHlsDataSourceFactory(dataSourceFactory))
                    .createMediaSource(uri)
            }
            C.TYPE_OTHER -> {
                ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            }
            else -> {
                throw PlayerException("Unsupported media source type: $type")
            }
        }
//        mediaSource.addEventListener(mainHandler, eventListener)
        return mediaSource
    }

    private val playerEventListener = object : Player.DefaultEventListener() {

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
            logger.v("timeline changed reason : $reason")
            updatePlayerSeekInfo()
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                com.google.android.exoplayer2.Player.STATE_BUFFERING -> {
                    if (getPlayerState() is PlayerState.Stopped) {
                        return
                    }
                    updatePlayerState(PlayerState.Buffering(playWhenReady))
                }

                com.google.android.exoplayer2.Player.STATE_READY -> {
                    updatePlayerSeekInfo()
                    if (!analyticsModel.isFirstTimePlayed) {
//                        initTrackSelectionHelper()
                        analyticsModel.isFirstTimePlayed = true
                    }
                    if (playWhenReady) {
                        updatePlayerState(PlayerState.Playing(-1))
                    } else {
                        if (pausedDueToContentSwitch) {
                            pausedDueToContentSwitch = false
                        } else {
                            updatePlayerState(PlayerState.Paused)
                        }
                    }
                }
                com.google.android.exoplayer2.Player.STATE_ENDED -> if (player.currentPosition >= player.duration && player.duration > 0) {
                    stopPlayerProgressUpdate()
                    //TODO Analytics
//                    sendPlayStopEventIfNeeded(true)
                    updatePlayerState(PlayerState.Finished(-1))
                }
                Player.STATE_IDLE -> {
                    if (playerIdleReasonStop) {
                        //idle due to player.stop()
                        stopPlayerProgressUpdate()
                        updatePlayerState(PlayerState.Stopped(true))
                        playerIdleReasonStop = false
                    }
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            super.onPlayerError(error)
            stopPlayerProgressUpdate()
            val playerError = PlayerError(error, getPlayerState())
            logger.e("playback error: $playerError")
            if (playerError.isRecoverable) {
                try {
                    retryPlayback(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
//                updatePlayerState(PlayerState.Error(playerError.errorCode, playerError.errorMsg, playerError.errorReason?.name))
                updatePlayerState(
                    PlayerState.Error
                        (
                        playerError.errorCode,
                        playerError.errorMsg ?: "Unknown error while playback",
                        "Can not play content, Please try again",
                        true,
                        stackTrace = playerError.stackTrace
                    )
                )
            }
        }

        override fun onPositionDiscontinuity(reason: Int) {
            super.onPositionDiscontinuity(reason)
            if (player.currentWindowIndex > 0 && reason == DISCONTINUITY_REASON_PERIOD_TRANSITION) {
                //content switched due to playlist
                logger.d("VISION Starting new content")
                pausedDueToContentSwitch = true
                player.playWhenReady = false
                logger.i("started playing next content in playlist")
                currentPlaybackItem = nextPlaybackItem
                currentPlayerConfig?.let {
                    updatePlayerConfig(it)
                }
                seekPositionInMs = Math.max(
                    0, currentPlaybackItem?.resumePointInMilliSeconds
                        ?: 0
                )
                updatePlayerState(PlayerState.Finished(-1))
//                    updatePlayerState(PlayerState.Idle)
            }
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray?,
            trackSelections: TrackSelectionArray?
        ) {
            super.onTracksChanged(trackGroups, trackSelections)
            //manifest loaded
            val current = player.videoFormat
            logger.d("tracks changed $current")
            analyticsModel.hasManifestBeenFetched = true
//            analyticsModel.manifestFetchTime = loadTime
        }
    }

    private val updateProgressAction = Runnable { updatePlayerSeekInfo() }

    private fun updatePlayerSeekInfo() {
        val currentPosition = player.currentPosition
        val duration = if (player.duration < 0) 0 else player.duration
        val bufferedPosition = player.bufferedPosition
        logger.d("player progress -> current position : ${player.currentPosition}, duration -> ${player.duration}")
        seekPositionInMs = currentPosition
        updatePlayerSeekInfo(PlayerSeekInfo(currentPosition, duration, bufferedPosition))
        stopPlayerProgressUpdate()
        if (isContentPlaying()) {
            mainHandler.postDelayed(updateProgressAction, seekUpdateInterval)
        }
    }

    private fun isContentPlaying(): Boolean {
        return when (player.playbackState) {
            com.google.android.exoplayer2.Player.STATE_BUFFERING -> true
            com.google.android.exoplayer2.Player.STATE_READY -> {
                player.playWhenReady
            }
            else -> false
        }
    }

    private fun initTrackSelectionHelper() {
        if (trackSelector.currentMappedTrackInfo == null) {
            return
        }
        val rendererCount = trackSelector.currentMappedTrackInfo?.rendererCount ?: 0
        for (i in 0 until rendererCount) {
            val trackGroups = trackSelector.currentMappedTrackInfo?.getTrackGroups(i)
            if (trackGroups?.length != 0) {
                if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO) {
                    trackSelectionHelper?.setAvailableBitRates(i, trackGroups)
                    break
                }
            }
        }
    }

    private fun stopPlayerProgressUpdate() {
        mainHandler.removeCallbacks(updateProgressAction)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    inner class PlayerEventListener : DefaultAnalyticsListener() {

        override fun onLoadCompleted(
            eventTime: AnalyticsListener.EventTime,
            loadEventInfo: MediaSourceEventListener.LoadEventInfo,
            mediaLoadData: MediaSourceEventListener.MediaLoadData
        ) {
            super.onLoadCompleted(eventTime, loadEventInfo, mediaLoadData)
            if (mediaLoadData.dataType == C.DATA_TYPE_MEDIA) {
                logger.d("loaded segment uri : ${loadEventInfo.dataSpec.uri}")
//                PlayerAnalytics.onSegmentDownloaded(loadEventInfo, mediaLoadData)
            }
        }

        override fun onTracksChanged(
            eventTime: AnalyticsListener.EventTime?,
            trackGroups: TrackGroupArray?,
            trackSelections: TrackSelectionArray?
        ) {
            super.onTracksChanged(eventTime, trackGroups, trackSelections)
            initTrackSelectionHelper()
        }
    }
}
