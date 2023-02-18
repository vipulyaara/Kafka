package org.kafka.notifications

import android.app.Notification
import android.app.PendingIntent
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

// additional configuration for notification builder
typealias NotificationBuilder = NotificationCompat.Builder.() -> Unit

interface NotificationManager {
    fun buildNotification(
        push: Push,
        pendingIntent: PendingIntent?,
        notificationBuilder: NotificationBuilder = {}
    ): Notification

    fun showNotification(id: Int, notification: Notification?)

    fun cancelNotification(notificationId: Int)

    fun cancelAllNotifications()

    fun updateNotification(notificationBuilder: NotificationBuilder = {})
}

data class Push(
    val notification: Notification,
    val channel: Channel,
    val actions: List<Action>
) {
    data class Action(val title: String, @DrawableRes val resId: Int)
    data class Channel(val id: String, val name: String)
    data class Notification(
        val id: Int,
        val title: String,
        val message: String,
    )
}
