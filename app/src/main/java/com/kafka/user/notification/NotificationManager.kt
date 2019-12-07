//package com.kafka.user.notification
//
//import android.annotation.SuppressLint
//import android.app.Notification
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.Drawable
//import android.os.Build
//import androidx.core.app.NotificationCompat
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.SimpleTarget
//import com.kafka.player.model.PlaybackItem
//import com.kafka.user.R
//import com.kafka.user.feature.MainActivity
//import com.kafka.user.player.Player
//import com.kafka.user.player.PlayerCommand
//import com.kafka.user.player.PlayerService
//
///**
// * @author Vipul Kumar; dated 08/04/19.
// */
////class NotificationManaager {
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
//        val currentItem = Player.currentItem()
//        val intent = getActivityIntent(
//            context = context,
//            contentId = currentItem?.id,
//            imageUrl = ""
//        )
//        intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//
//        mShowActivityPendingIntent = PendingIntent
//            .getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        mTogglePlayPendingIntent = PendingIntent
//            .getService(
//                context, 0, Intent(context, PlayerService::class.java)
//                    .setAction(PlayerCommand.TOGGLE.toString()), 0
//            )
//        mPlayNextPendingIntent = PendingIntent
//            .getService(
//                context, 0, Intent(context, PlayerService::class.java)
//                    .setAction(PlayerCommand.NEXT.toString()), 0
//            )
//        mPlayPrevPendingIntent = PendingIntent
//            .getService(
//                context, 0, Intent(context, PlayerService::class.java)
//                    .setAction(PlayerCommand.PREV.toString()), 0
//            )
//        mStopPendingIntent = PendingIntent
//            .getService(
//                context, 0, Intent(context, PlayerService::class.java)
//                    .setAction(PlayerCommand.STOP.toString()), 0
//            )
//    }
//
//    private fun getActivityIntent(context: Context, contentId: String?, imageUrl: String): Intent? {
//        return Intent(context, MainActivity::class.java)
//    }
//
//    @SuppressLint("RestrictedApi") fun updateNotification(
//        context: Context,
//        item: PlaybackItem,
//        isPlaying: Boolean,
//        onNotificationUpdate: (Notification) -> Unit
//    ) {
//        preparePendingIntents(context)
//        musicNotificationBuilder?.mActions?.clear()
//        musicNotificationBuilder =
//            NotificationCompat.Builder(context)
//        var drawableIcon: Int? = null
//        drawableIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            R.drawable.ic_arrow_back_black_24dp
//        } else {
//            R.drawable.ic_arrow_back_black_24dp
//        }
//        musicNotificationBuilder?.setSmallIcon(drawableIcon)!!
//            .setContentText(item.title)
//            .setSubText(item.title)
//            .setContentTitle(item.title)
//            .setContentIntent(mShowActivityPendingIntent)
//            .setDeleteIntent(mStopPendingIntent)
//            .setWhen(0)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//
//        var buttonCount = 0
//        musicNotificationBuilder?.addAction(
//            R.drawable.ic_skip_previous_black_24dp,
//            context.getString(R.string.previous),
//            mPlayPrevPendingIntent
//        )
//        buttonCount++
//
//        if (isPlaying) {
//            musicNotificationBuilder?.addAction(
//                R.drawable.ic_pause_black_24dp,
//                context.getString(R.string.pause),
//                mTogglePlayPendingIntent
//            )
//            buttonCount++
//        } else {
//            musicNotificationBuilder?.addAction(
//                R.drawable.ic_pause_black_24dp,
//                context.getString(R.string.play),
//                mTogglePlayPendingIntent
//            )
//            buttonCount++
//        }
//
//        musicNotificationBuilder?.addAction(
//            R.drawable.ic_skip_next_black_24dp,
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
////            musicNotificationBuilder?.setStyle(
////                Notification.MediaStyle()
////                    .setMediaSession(mMediaSession.getSessionToken())
////                    .setShowCancelButton(true)
//////                    .setCancelBuattonIntent(mStopPendingIntent)
////                    .setShowActionsInCompactView(*compactControls)
////            )
//        }
//        if (musicImageUrl == null) {
//            val option = BitmapFactory.Options()
//            option.inSampleSize = 2
//            musicNotificationBuilder?.setLargeIcon(
//                BitmapFactory.decodeResource(
//                    context.resources,
//                    R.drawable.ic_arrow_back_black_24dp,
//                    option
//                )
//            )
//            onNotificationUpdate.invoke(musicNotificationBuilder!!.build())
//        }
//
//        musicImageUrl = item.imageUrl
//        loadImage(item.imageUrl, context, object : OnImageLoadedListener {
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
//                        R.drawable.ic_arrow_back_black_24dp,
//                        option
//                    )
//                )
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
//                    onImageLoadedListener.onLoadFailed()
//                }
//
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
//                ) {
//                    onImageLoadedListener.onImageLoaded(resource)
//                }
//            })
//    }
//
//    interface OnImageLoadedListener {
//        fun onImageLoaded(resource: Bitmap)
//
//        fun onLoadFailed()
//    }
//}
//
