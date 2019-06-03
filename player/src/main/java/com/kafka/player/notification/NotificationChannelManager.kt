package com.kafka.player.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

/**
 * Created by Prashant Kumar on 5/25/18.
 */
object NotificationChannelManager {

    interface OnNotificationUpdateListener {

        fun onNotificationUpdate(notification: Notification)
    }

    fun getChannel(context: Context, notificationChannelType: NotificationChannelType): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return NotificationCompat.Builder(context)
        }
        return NotificationCompat.Builder(context, createNotificationChannel(context, notificationChannelType))
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context, notificationChannelType: NotificationChannelType): String {
        val channelId = getChannelId(notificationChannelType)
        val channelName = getChannelName(notificationChannelType)
        var priority = NotificationManager.IMPORTANCE_DEFAULT
        if (notificationChannelType == NotificationChannelType.PLAYER ||
                notificationChannelType == NotificationChannelType.DOWNLOAD ||
                notificationChannelType == NotificationChannelType.ROLLED_UP ||
                notificationChannelType == NotificationChannelType.SLEEP_TIMER) {
            priority = NotificationManager.IMPORTANCE_LOW
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, channelName, priority)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = service.getNotificationChannel(getChannelId(notificationChannelType))
            if (notificationChannel === null)
                service.createNotificationChannel(chan)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return channelId
    }

    private fun getChannelName(notificationChannelType: NotificationChannelType): String {
        return when (notificationChannelType) {
            NotificationChannelType.PLAYER -> "Player Notification"
            NotificationChannelType.FUP_LIMIT -> "Default Notification"
            NotificationChannelType.DOWNLOAD -> "Song Download Notification"
            NotificationChannelType.COMMON -> "General Notification"
            NotificationChannelType.HOTSPOT -> "Wynk Direct Hotspot Notification"
            NotificationChannelType.ROLLED_UP -> "Wynk Rolled Up Notification"
            NotificationChannelType.SLEEP_TIMER -> "Sleep Timer Notification"
        }
    }

    private fun getChannelId(notificationChannelType: NotificationChannelType): String {
        val id = when (notificationChannelType) {
            NotificationChannelType.FUP_LIMIT -> "player_fup_channel"
            NotificationChannelType.PLAYER -> "player_service_channel"
            NotificationChannelType.DOWNLOAD -> "download_service_channel"
            NotificationChannelType.COMMON -> "common_notification_channel"
            NotificationChannelType.HOTSPOT -> "hotspot_service_channel"
            NotificationChannelType.ROLLED_UP -> "rolled_up_notification_channel"
            NotificationChannelType.SLEEP_TIMER -> "sleep_timer"
        }
        return id
    }
}

public enum class NotificationChannelType {
    COMMON, PLAYER, FUP_LIMIT, DOWNLOAD, HOTSPOT, ROLLED_UP, SLEEP_TIMER
}
