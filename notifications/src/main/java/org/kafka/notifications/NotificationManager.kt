package org.kafka.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

// additional configuration for notification builder
typealias NotificationBuilder = NotificationCompat.Builder.() -> Unit

// additional configuration for content intent
typealias IntentBuilder = Intent.() -> Unit

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

    companion object {
        const val NOTIFICATION_ID_DOWNLOAD_FILE = 101
    }
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

private fun NotificationManager.showNotification() {
    val push = Push(
        Push.Notification(
            100,
            "You have a new delivery",
            "An error occurred while accepting delivery. Please try again."
        ),
        Push.Channel("channel", "Delivery"),
        listOf(
            Push.Action("Accept Delivery", R.drawable.ic_editors_choice)
        )
    )

    val notification = buildNotification(push, null) {
        setSmallIcon(R.drawable.ic_rekhta_r)
        setProgress(100, 40, false)
    }

    showNotification(100, notification)
}
