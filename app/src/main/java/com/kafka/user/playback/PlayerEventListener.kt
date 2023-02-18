package com.kafka.user.playback

import com.sarahang.playback.core.apis.PlayerEventLogger
import org.kafka.analytics.Analytics
import org.kafka.analytics.EventInfo
import javax.inject.Inject

class KafkaPlayerEventLogger @Inject constructor(
    private val analytics: Analytics
) : PlayerEventLogger {
    override fun logEvent(event: String, data: Map<String, String>) {
        analytics.log(EventInfo(event, data))
    }
}
