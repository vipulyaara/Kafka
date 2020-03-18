package com.kafka.user.notification

//package com.kafka.user.feature.notification
//
//import  android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.Drawable
//import android.os.Build
//import android.transition.Transition
//import androidx.annotation.NonNull
//import androidx.core.app.NotificationCompat
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.target.SimpleTarget
//import com.bumptech.glide.request.target.Target
//import com.kafka.player.R
//import com.kafka.player.model.PlaybackItem
//import com.kafka.player.notification.NotificationChannelManager
//import com.kafka.player.notification.NotificationChannelType
//import com.kafka.player.service.PlayerService
//import com.kafka.player.service.PlayerServiceBridge
//import com.kafka.user.player.PlayerService
//
//object NotificationHelper {
//    private var mTogglePlayPendingIntent: PendingIntent? = null
//    private var mPlayNextPendingIntent: PendingIntent? = null
//    private var mPlayPrevPendingIntent: PendingIntent? = null
//    private var mStopPendingIntent: PendingIntent? = null
//    private var musicNotificationBuilder: NotificationCompat.Builder? = null
//    private var musicImageUrl: String? = null
//
//    private var mShowActivityPendingIntent: PendingIntent? = null
//
//    private fun preparePendingIntents(context: Context) {
//        val intent = DetailActivity.getActivityIntent(
//            context = context,
//            contentId = PlayerServiceBridge.getInstance().contentId,
//            packId = PlayerServiceBridge.getInstance().packageId,
//            contentType = PlayerServiceBridge.getInstance().contentType,
//            imageUrl = "",
//            sourceName = PlayerServiceBridge.getInstance().getSourceName() ?: "Wynk Music"
//        )
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//
//        mShowActivityPendingIntent = PendingIntent
//            .getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        mTogglePlayPendingIntent = PendingIntent
//            .getService(
//                context, 0, Intent(context, PlayerService::class.java)
//                    .setAction(AudioFocusManager.PlayerCommand.TOGGLE.toString()), 0
//            )
//        mPlayNextPendingIntent = PendingIntent
//            .getService(
//                context, 0, Intent(context, PlayerService::class.java)
//                    .setAction(AudioFocusManager.PlayerCommand.NEXT.toString()), 0
//            )
//        mPlayPrevPendingIntent = PendingIntent
//            .getService(
//                context, 0, Intent(context, PlayerService::class.java)
//                    .setAction(AudioFocusManager.PlayerCommand.PREV.toString()), 0
//            )
//        mStopPendingIntent = PendingIntent
//            .getService(
//                context, 0, Intent(context, PlayerService::class.java)
//                    .setAction(AudioFocusManager.PlayerCommand.STOP.toString()), 0
//            )
//    }
//
//    fun updateNotification(
//        context: Context,
//        song: PlaybackItem,
//        mediaSessionToken: MediaSessionCompat.Token?,
//        playerState: Int,
//        onNotificationUpdate: NotificationChannelManager.OnNotificationUpdateListener
//    ) {
//        preparePendingIntents(context)
//        musicNotificationBuilder?.mActions?.clear()
//        musicNotificationBuilder =
//                NotificationChannelManager.getChannel(context, NotificationChannelType.PLAYER)
//        var drawableIcon: Int? = null
//        drawableIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            R.drawable.notification_bar_icon_lollipop
//        } else {
//            R.drawable.notification_bar_icon
//        }
//        musicNotificationBuilder?.setSmallIcon(drawableIcon)!!
//            .setContentText(song.title)
//            .setSubText(song.title)
//            .setContentTitle(song.title)
//            .setContentIntent(mShowActivityPendingIntent)
//            .setDeleteIntent(mStopPendingIntent)
//            .setWhen(0)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//
//        var buttonCount = 0
//        musicNotificationBuilder?.addAction(
//            R.drawable.prev_white,
//            context.getString(R.string.previous),
//            mPlayPrevPendingIntent
//        )
//        buttonCount++
//
//        if (playerState == IPlayer.STATE_PLAYING || playerState == IPlayer.STATE_READY) {
//            musicNotificationBuilder?.addAction(
//                R.drawable.pause_white,
//                context.getString(R.string.pause),
//                mTogglePlayPendingIntent
//            )
//            buttonCount++
//        } else {
//            musicNotificationBuilder?.addAction(
//                R.drawable.play_white,
//                context.getString(R.string.play),
//                mTogglePlayPendingIntent
//            )
//            buttonCount++
//        }
//
//        musicNotificationBuilder?.addAction(
//            R.drawable.next_white,
//            context.getString(R.string.next),
//            mPlayNextPendingIntent
//        )
//        buttonCount++
//
//        val compactControls = IntArray(buttonCount)
//        for (i in 0 until buttonCount) {
//            compactControls[i] = i
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            musicNotificationBuilder?.setStyle(
//                NotificationCompat.MediaStyle()
//                    .setMediaSession(mediaSessionToken)
//                    .setShowCancelButton(true)
//                    .setCancelButtonIntent(mStopPendingIntent)
//                    .setShowActionsInCompactView(*compactControls)
//            )
//        }
//        if (musicImageUrl == null) {
//            val option = BitmapFactory.Options()
//            option.inSampleSize = 2
//            musicNotificationBuilder?.setLargeIcon(
//                BitmapFactory.decodeResource(
//                    context.resources,
//                    R.drawable.ic_placeholder_330x330,
//                    option
//                )
//            )
//            onNotificationUpdate.onNotificationUpdate(musicNotificationBuilder!!.build())
//        }
//
//        musicImageUrl = song.images?.portrait.toString()
//        loadImage(song.images?.portrait.toString(), context, object : OnImageLoadedListener {
//            override fun onImageLoaded(resource: Bitmap) {
//                musicNotificationBuilder?.setLargeIcon(resource)
//            }
//
//            override fun onLoadFailed() {
//                val option = BitmapFactory.Options()
//                option.inSampleSize = 2
//                musicNotificationBuilder?.setLargeIcon(
//                    BitmapFactory.decodeResource(
//                        context.resources,
//                        R.drawable.ic_placeholder_330x330,
//                        option
//                    )
//                )
//                if (FUManager.getInstance().isFULimitReached?.value == false) {
//                    onNotificationUpdate.onNotificationUpdate(musicNotificationBuilder!!.build())
//                }
//            }
//        })
//    }
//
//    private fun loadImage(
//        imageUrl: String,
//        context: Context,
//        onImageLoadedListener: OnImageLoadedListener
//    ) {
//        Glide.with(context)
//            .asBitmap()
//            .load(imageUrl)
//            .into(object : SimpleTarget<Bitmap>() {
//                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    onImageLoadedListener?.onLoadFailed()
//                }
//
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
//                ) {
//                    onImageLoadedListener?.onImageLoaded(resource)
//                }
//            })
//    }
//
//    fun showFUEndNotification(@NonNull context: Context, onNotificationUpdate: NotificationChannelManager.OnNotificationUpdateListener) {
//        val intent = DetailActivity.getActivityIntent(
//            context = context,
//            contentId = PlayerServiceBridge.getInstance().contentId,
//            packId = PlayerServiceBridge.getInstance().packageId,
//            contentType = PlayerServiceBridge.getInstance().contentType,
//            imageUrl = "",
//            sourceName = PlayerServiceBridge.getInstance().getSourceName() ?: "Wynk Music"
//        )
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        mShowActivityPendingIntent = PendingIntent
//            .getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val builder =
//            NotificationChannelManager.getChannel(context, NotificationChannelType.FUP_LIMIT)
//        var drawableIcon: Int? = null
//        drawableIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            R.drawable.notification_bar_icon_lollipop
//        } else {
//            R.drawable.notification_bar_icon
//        }
//        builder.setSmallIcon(drawableIcon)
//            .setAutoCancel(true)
//            .setContentText(context.getString(R.string.fu_end_content))
//            .setSubText(
//                PlayerServiceBridge.getInstance().currentSong()?.title ?: context.getString(
//                    R.string.song
//                )
//            )
//            .setContentTitle(context.getString(R.string.fu_end_titile))
//            .setContentIntent(mShowActivityPendingIntent)
//            .setWhen(0)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//        onNotificationUpdate.onNotificationUpdate(builder.build())
//    }
//
//    interface OnImageLoadedListener {
//        fun onImageLoaded(resource: Bitmap)
//
//        fun onLoadFailed()
//    }
//}
//
