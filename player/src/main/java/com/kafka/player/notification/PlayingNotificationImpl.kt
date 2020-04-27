//package com.kafka.player.notification
//
//import android.app.Notification
//import android.app.PendingIntent
//import android.content.ComponentName
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.os.Build
//import android.text.Html
//import androidx.core.app.NotificationCompat
//import androidx.media.app.NotificationCompat.MediaStyle
//import androidx.palette.graphics.Palette
//import code.name.monkey.retromusic.R
//import code.name.monkey.retromusic.activities.MainActivity
//import code.name.monkey.retromusic.glide.SongGlideRequest
//import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
//import code.name.monkey.retromusic.service.MusicService
//import code.name.monkey.retromusic.service.MusicService.*
//import code.name.monkey.retromusic.util.MusicUtil
//import code.name.monkey.retromusic.util.PreferenceUtil
//import code.name.monkey.retromusic.util.RetroColorUtil
//import coil.bitmappool.BitmapPool
//import coil.size.Size
//import coil.transform.Transformation
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.animation.GlideAnimation
//import com.bumptech.glide.request.target.SimpleTarget
//import com.bumptech.glide.request.target.Target
//import com.kafka.player.R
//import com.kafka.player.service.MusicService
//import com.kafka.player.service.MusicService.*
//
//class PlayingNotificationImpl : PlayingNotification() {
//    @Synchronized
//    override fun update() {
//        stopped = false
//
//        val song = service.currentSong
//        val isPlaying = service.isPlaying
//        val playButtonResId = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
//
//        val action = Intent(service, MainActivity::class.java)
//        action.putExtra(MainActivity.EXPAND_PANEL, true)
//        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        val clickIntent =
//            PendingIntent.getActivity(service, 0, action, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val serviceName = ComponentName(service, MusicService::class.java)
//        val intent = Intent(ACTION_QUIT)
//        intent.component = serviceName
//        val deleteIntent = PendingIntent.getService(service, 0, intent, 0)
//
//        val bigNotificationImageSize = service.resources
//            .getDimensionPixelSize(R.dimen.notification_big_image_size)
//        service.runOnUiThread {
//            fun update(bitmap: Bitmap?, color: Int) {
//                var bitmapFinal = bitmap
//                if (bitmapFinal == null) {
//                    bitmapFinal = BitmapFactory.decodeResource(service.resources, R.drawable.default_audio_art)
//                }
//
//                val playPauseAction = NotificationCompat.Action(
//                    playButtonResId,
//                    service.getString(R.string.action_play_pause),
//                    retrievePlaybackAction(ACTION_TOGGLE_PAUSE)
//                )
//                val nextAction = NotificationCompat.Action(
//                    R.drawable.ic_skip_next_round_white_32dp,
//                    service.getString(R.string.action_next),
//                    retrievePlaybackAction(ACTION_SKIP)
//                )
//
//                val builder = NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID)
//                    .setSmallIcon(R.drawable.ic_notification)
//                    .setLargeIcon(bitmapFinal)
//                    .setContentIntent(clickIntent)
//                    .setDeleteIntent(deleteIntent)
//                    .setContentTitle(Html.fromHtml("<b>" + song.title + "</b>"))
//                    .setContentText(song.artistName)
//                    .setSubText(Html.fromHtml("<b>" + song.albumName + "</b>"))
//                    .setOngoing(isPlaying)
//                    .setShowWhen(false)
//                    .addAction(playPauseAction)
//                    .addAction(nextAction)
//
//                builder.setStyle(
//                    NotificationCompat.MediaStyle()
//                        .setMediaSession(service.mediaSession.sessionToken)
//                        .setShowActionsInCompactView(1, 2, 3)
//                ).setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
//                    builder.color = color
//                }
//
//                if (stopped) {
//                    return  // notification has been stopped before loading was finished
//                }
//                updateNotifyModeAndPostNotification(builder.build())
//            }
//        }
//    }
//
//    private fun retrievePlaybackAction(action: String): PendingIntent {
//        val serviceName = ComponentName(service, MusicService::class.java)
//        val intent = Intent(action)
//        intent.component = serviceName
//        return PendingIntent.getService(service, 0, intent, 0)
//    }
//
//    private fun coilPaletteTransformation() = object : Transformation {
//        override fun key() = "paletteTransformer"
//        override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
//            val p = Palette.from(input).generate()
//            val swatch = p.vibrantSwatch
//            return input
//        }
//    }
//}
