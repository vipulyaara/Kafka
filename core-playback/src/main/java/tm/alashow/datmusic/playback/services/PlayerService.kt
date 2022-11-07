/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.playback.services

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.kafka.data.dao.AudioDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import timber.log.Timber
import tm.alashow.datmusic.playback.*
import tm.alashow.datmusic.playback.models.MediaId
import tm.alashow.datmusic.playback.models.MediaId.Companion.CALLER_OTHER
import tm.alashow.datmusic.playback.models.MediaId.Companion.CALLER_SELF
import tm.alashow.datmusic.playback.models.toAudioList
import tm.alashow.datmusic.playback.models.toMediaId
import tm.alashow.datmusic.playback.models.toMediaItems
import tm.alashow.datmusic.playback.players.RekhtaPlayerImpl
import tm.alashow.datmusic.playback.receivers.BecomingNoisyReceiver
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : MediaBrowserServiceCompat(), CoroutineScope by MainScope() {

    companion object {
        var IS_FOREGROUND = false
    }

    @Inject
    protected lateinit var dispatchers: AppCoroutineDispatchers

    @Inject
    protected lateinit var rekhtaPlayer: RekhtaPlayerImpl

    @Inject
    protected lateinit var mediaNotifications: MediaNotificationsImpl

    @Inject
    protected lateinit var audiosDao: AudioDao

    private lateinit var becomingNoisyReceiver: BecomingNoisyReceiver

    override fun onCreate() {
        super.onCreate()

        sessionToken = rekhtaPlayer.getSession().sessionToken
        becomingNoisyReceiver = BecomingNoisyReceiver(this, sessionToken!!)

        rekhtaPlayer.onPlayingState { isPlaying, byUi ->
            val isIdle = rekhtaPlayer.getSession().controller.playbackState.isIdle
            if (!isPlaying && isIdle) {
                pauseForeground(byUi)
                mediaNotifications.clearNotifications()
            } else {
                startForeground()
            }

            mediaNotifications.updateNotification(getSession())
        }

        rekhtaPlayer.onMetaDataChanged {
            mediaNotifications.updateNotification(getSession())
        }
    }

    private fun startForeground() {
        if (IS_FOREGROUND) {
            Timber.w("Tried to start foreground, but was already in foreground")
            return
        }
        Timber.d("Starting foreground service")
        startForeground(
            NOTIFICATION_ID,
            mediaNotifications.buildNotification(rekhtaPlayer.getSession())
        )
        becomingNoisyReceiver.register()
        IS_FOREGROUND = true
    }

    private fun pauseForeground(removeNotification: Boolean) {
        if (!IS_FOREGROUND) {
            Timber.w("Tried to stop foreground, but was already NOT in foreground")
            return
        }
        Timber.d("Stopping foreground service")
        becomingNoisyReceiver.unregister()
        stopForeground(removeNotification)
        IS_FOREGROUND = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            return START_STICKY
        }

        val mediaSession = rekhtaPlayer.getSession()
        val controller = mediaSession.controller

        when (intent.action) {
            PLAY_PAUSE -> controller.playPause()
            NEXT -> controller.transportControls.skipToNext()
            PREVIOUS -> controller.transportControls.skipToPrevious()
            STOP_PLAYBACK -> controller.transportControls.stop()
        }

        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return START_STICKY
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        val caller =
            if (clientPackageName == applicationContext.packageName) CALLER_SELF else CALLER_OTHER
        return BrowserRoot(MediaId("-1", caller = caller).toString(), null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.detach()
        launch {
            val itemList = withContext(dispatchers.io) { loadChildren(parentId) }
            result.sendResult(itemList)
        }
    }

    private suspend fun loadChildren(parentId: String): MutableList<MediaBrowserCompat.MediaItem> {
        val list = mutableListOf<MediaBrowserCompat.MediaItem>()
        val mediaId = parentId.toMediaId()
        list.addAll(mediaId.toAudioList(audiosDao).toMediaItems())
        return list
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        launch {
            rekhtaPlayer.pause()
            rekhtaPlayer.saveQueueState()
            rekhtaPlayer.stop(false)
        }
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        launch {
            rekhtaPlayer.saveQueueState()
            rekhtaPlayer.release()
        }
    }
}
