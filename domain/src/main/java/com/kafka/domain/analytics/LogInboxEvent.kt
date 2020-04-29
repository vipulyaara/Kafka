package com.kafka.domain.analytics

import com.kafka.data.injection.ProcessLifetime
import com.kafka.logger.Event
import com.kafka.logger.EventInfo
import com.kafka.logger.Logger
import com.kafka.logger.LoggingInteractor
import dagger.Reusable
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * Logs all the events related to inbox.
 * Usage:
 *
 * @Inject logInboxEvent: LogInboxEvent
 *
 * logInboxEvent { notificationDetailClick(id) }
 *
 * */
class LogInboxEvent @Inject constructor(
    @ProcessLifetime override val scope: CoroutineScope,
    override val logger: Logger,
    override val event: ContentEvent
) : LoggingInteractor<ContentEvent>()

@Reusable
class ContentEvent @Inject constructor(
    private val contentAnalyticsRepository: ContentAnalyticsRepository
) : Event {

    fun notificationDetailClick(notificationId: String): EventInfo =
        "notification_detail_click" to contentAnalyticsRepository.notificationDetailClickData(notificationId)

    fun inboxScreenOpen(): EventInfo =
        "inbox_screen_open" to contentAnalyticsRepository.inboxScreenOpenData()
}
