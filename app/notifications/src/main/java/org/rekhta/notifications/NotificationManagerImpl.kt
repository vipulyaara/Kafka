package org.rekhta.notifications

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotificationManagerImpl(
    application: Application,
    private val notificationManager: NotificationManagerCompat
) : NotificationManager {

    private val context = application.applicationContext

    override fun showNotification(id: Int, notification: Notification?) {
        notification?.let { notificationManager.notify(id, it) }
    }

    override fun buildNotification(
        push: Push,
        pendingIntent: PendingIntent?,
        notificationBuilder: NotificationBuilder,
    ): Notification {
        val channel = push.channel
        val notification = push.notification

        createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(
            context, channel.id
        ).apply {
            setContentTitle(notification.title)
            setContentText(notification.message)
            setContentIntent(pendingIntent)

            // overrides any configuration set for the builder
            notificationBuilder()
        }

        push.actions.forEach {
            builder.addAction(it.resId, it.title, mapIntent)
        }

        return builder.build()
    }

    private val mapIntent: PendingIntent
        get() {
            val gmmIntentUri: Uri = Uri.parse("google.streetview:cbll=46.414382,10.013988")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            return PendingIntent.getActivity(
                context,
                100,
                mapIntent,
                0
            )
        }

    private fun createNotificationChannel(channel: Push.Channel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getNotificationChannel(channel.id) == null) {
                val notificationChannel = NotificationChannel(
                    channel.id,
                    channel.name,
                    IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun getNotificationChannel(channelId: String): NotificationChannel? {
        return notificationManager.getNotificationChannel(channelId)
    }

    override fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    override fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }
}
