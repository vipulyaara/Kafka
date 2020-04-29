package com.kafka.domain.analytics

import com.kafka.data.logger.AnalyticsRepository
import javax.inject.Inject

class ContentAnalyticsRepository @Inject constructor(
    analyticsRepository: AnalyticsRepository
){
    fun notificationDetailClickData(notificationId: String) = mapOf(
        "notification_id" to notificationId
    )

    fun inboxScreenOpenData() = mapOf(
        "screen_id" to "inbox"
    )
}
