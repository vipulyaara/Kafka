package com.kafka.content.data

import javax.inject.Inject

class ContentAnalyticsRepository @Inject constructor() {
    fun notificationDetailClickData(notificationId: String) = mapOf(
        "notification_id" to notificationId
    )

    fun inboxScreenOpenData() = mapOf(
        "screen_id" to "inbox"
    )
}
