package com.kafka.user.fcm

import com.google.firebase.messaging.RemoteMessage
import org.kafka.notifications.Push
import javax.inject.Inject

class PushMapper @Inject constructor() {
    fun map(remoteMessage: RemoteMessage) = Push(
        notification = Push.Notification(
            id = remoteMessage.messageId.hashCode(),
            title = remoteMessage.notification?.title.orEmpty(),
            message = remoteMessage.notification?.body.orEmpty(),
        ),
        channel = Push.Channel(
            id = remoteMessage.messageId.orEmpty(),
            name = remoteMessage.notification?.title.orEmpty(),
        ),
        actions = emptyList()
    )
}
