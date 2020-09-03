//package com.kafka.player.notification
//
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.NotificationManager.IMPORTANCE_LOW
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.support.v4.media.MediaMetadataCompat.*
//import android.support.v4.media.session.MediaSessionCompat
//import android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP
//import androidx.annotation.IdRes
//import androidx.core.app.NotificationCompat
//import androidx.core.graphics.toColorInt
//import androidx.media.session.MediaButtonReceiver.buildMediaButtonPendingIntent
//import androidx.palette.graphics.Palette
//import com.kafka.data.CustomScope
//import com.kafka.player.R
//import com.kafka.player.timber.Utils.isOreo
//import com.kafka.player.timber.constants.Constants.ACTION_NEXT
//import com.kafka.player.timber.constants.Constants.ACTION_PLAY_PAUSE
//import com.kafka.player.timber.constants.Constants.ACTION_PREVIOUS
//import com.kafka.player.timber.playback.MusicService
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import java.lang.System.currentTimeMillis
//import javax.inject.Inject
//import javax.inject.Singleton
//import androidx.media.app.NotificationCompat as NotificationMediaCompat
//
//private const val CHANNEL_ID = "playback_channel"
//private const val NOTIFICATION_ID = 888
//
//interface Notifications {
//    fun updateNotification(mediaSession: MediaSessionCompat)
//    fun buildNotification(mediaSession: MediaSessionCompat): Notification
//}
//
//@Singleton
//class RealNotifications @Inject constructor(
//    @ApplicationContext private val context: Context,
//    private val notificationManager: NotificationManager
//) : Notifications, CoroutineScope by CustomScope() {
//    private var postTime: Long = -1
//
//    override fun updateNotification(mediaSession: MediaSessionCompat) {
//        // TODO should this be scoped so that it can be cancelled?
//        GlobalScope.launch {
//            notificationManager.notify(NOTIFICATION_ID, buildNotification(mediaSession))
//        }
//    }
//
//    override fun buildNotification(mediaSession: MediaSessionCompat): Notification {
//        if (mediaSession.controller.metadata == null || mediaSession.controller.playbackState == null) {
//            return getEmptyNotification()
//        }
//
//        val albumName = mediaSession.controller.metadata.getString(METADATA_KEY_ALBUM)
//        val artistName = mediaSession.controller.metadata.getString(METADATA_KEY_ARTIST)
//        val trackName = mediaSession.controller.metadata.getString(METADATA_KEY_TITLE)
//        val artwork = mediaSession.controller.metadata.getBitmap(METADATA_KEY_ALBUM_ART)
//        val isPlaying = mediaSession.isPlaying()
//
//        val playButtonResId = if (isPlaying) {
//            R.drawable.ic_pause
//        } else {
//            R.drawable.ic_play
//        }
//
////        val nowPlayingIntent = Intent(context, MainActivity::class.java)
////        val clickIntent = PendingIntent.getActivity(context, 0, nowPlayingIntent, FLAG_UPDATE_CURRENT)
//
//        if (postTime == -1L) {
//            postTime = currentTimeMillis()
//        }
//        createNotificationChannel()
//
//        val style = NotificationMediaCompat.MediaStyle()
//            .setMediaSession(mediaSession.sessionToken)
//            .setShowCancelButton(true)
//            .setShowActionsInCompactView(0, 1, 2)
//            .setCancelButtonIntent(buildMediaButtonPendingIntent(context, ACTION_STOP))
//
//        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
//            setStyle(style)
//            setSmallIcon(R.drawable.ic_play)
//            setLargeIcon(artwork)
////            setContentIntent(clickIntent)
//            setContentTitle(trackName)
//            setContentText(artistName)
//            setSubText(albumName)
//            setColorized(true)
//            setShowWhen(false)
//            setWhen(postTime)
//            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            setDeleteIntent(buildMediaButtonPendingIntent(context, ACTION_STOP))
//            addAction(getPreviousAction(context))
//            addAction(getPlayPauseAction(context, playButtonResId))
//            addAction(getNextAction(context))
//        }
//
//        if (artwork != null) {
//            builder.color = Palette.from(artwork)
//                .generate()
//                .getVibrantColor("#403f4d".toColorInt())
//        }
//
//        return builder.build()
//    }
//
//    private fun getPreviousAction(context: Context): NotificationCompat.Action {
//        val actionIntent = Intent(context, MusicService::class.java).apply {
//            action = ACTION_PREVIOUS
//        }
//        val pendingIntent = PendingIntent.getService(context, 0, actionIntent, 0)
//        return NotificationCompat.Action(R.drawable.ic_step_backward, "", pendingIntent)
//    }
//
//    private fun getPlayPauseAction(context: Context, @IdRes playButtonResId: Int): NotificationCompat.Action {
//        val actionIntent = Intent(context, MusicService::class.java).apply {
//            action = ACTION_PLAY_PAUSE
//        }
//        val pendingIntent = PendingIntent.getService(context, 0, actionIntent, 0)
//        return NotificationCompat.Action(playButtonResId, "", pendingIntent)
//    }
//
//    private fun getNextAction(context: Context): NotificationCompat.Action {
//        val actionIntent = Intent(context, MusicService::class.java).apply {
//            action = ACTION_NEXT
//        }
//        val pendingIntent = PendingIntent.getService(context, 0, actionIntent, 0)
//        return NotificationCompat.Action(R.drawable.ic_skip_forward, "", pendingIntent)
//    }
//
//    private fun getEmptyNotification(): Notification {
//        createNotificationChannel()
//        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
//            setSmallIcon(R.drawable.ic_play)
//            setContentTitle("Loading audio...")
//            setColorized(true)
//            setShowWhen(false)
//            setWhen(postTime)
//            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//        }.build()
//    }
//
//    private fun createNotificationChannel() {
//        if (!isOreo()) return
//        val name = context.getString(R.string.media_playback)
//        val channel = NotificationChannel(CHANNEL_ID, name, IMPORTANCE_LOW).apply {
//            description = context.getString(R.string.media_playback_controls)
//            setShowBadge(false)
//            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        }
//        notificationManager.createNotificationChannel(channel)
//    }
//}
