@file:Suppress("ktlint:standard:filename")

package com.kafka.user.playback

import com.sarahang.playback.core.apis.Logger
import com.sarahang.playback.core.apis.PlayerEventLogger
import org.kafka.play.logger.Analytics
import org.kafka.play.logger.EventInfo
import javax.inject.Inject

class KafkaPlayerEventLogger @Inject constructor(
    private val analytics: Analytics,
) : PlayerEventLogger {
    override fun logEvent(event: String, data: Map<String, String>) {
        analytics.log(EventInfo(event, data))
    }
}

class PlayerLogger @Inject constructor() : Logger {
    override fun i(message: String) = org.kafka.base.i { message }

    override fun d(message: String) = org.kafka.base.debug { message }

    override fun w(message: String) = org.kafka.base.w { message }

    override fun e(message: String) = org.kafka.base.errorLog { message }

    override fun e(throwable: Throwable, message: String) =
        org.kafka.base.errorLog(throwable) { message }

}
