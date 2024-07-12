@file:Suppress("ktlint:standard:filename")
package com.kafka.user.playback

import com.sarahang.playback.core.apis.PlayerEventLogger
import org.kafka.play.logger.Analytics
import org.kafka.play.logger.EventInfo
import javax.inject.Inject

class KafkaPlayerEventLogger @Inject constructor(
    private val analytics: Analytics
) : PlayerEventLogger {
    override fun logEvent(event: String, data: Map<String, String>) {
        analytics.log(EventInfo(event, data))
    }
}
