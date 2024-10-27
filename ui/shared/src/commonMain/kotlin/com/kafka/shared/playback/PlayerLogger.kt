@file:Suppress("ktlint:standard:filename")

package com.kafka.shared.playback

import com.kafka.analytics.providers.Analytics
import com.kafka.analytics.providers.EventInfo
import com.kafka.base.debug
import com.kafka.base.errorLog
import com.sarahang.playback.core.apis.Logger
import com.sarahang.playback.core.apis.PlayerEventLogger
import me.tatarka.inject.annotations.Inject

@Inject
class KafkaPlayerEventLogger(
    private val analytics: Analytics,
) : PlayerEventLogger {
    override fun logEvent(event: String, data: Map<String, String>) {
        analytics.log(EventInfo(event, data))
    }
}

@Inject
class PlayerLogger : Logger {
    override fun i(message: String) = com.kafka.base.i { message }

    override fun d(message: String) = debug { message }

    override fun w(message: String) = com.kafka.base.w { message }

    override fun e(message: String) = errorLog { message }

    override fun e(throwable: Throwable, message: String) =
        errorLog(throwable) { message }

}
