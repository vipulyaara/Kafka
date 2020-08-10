package com.kafka.logger.domain

import com.kafka.logger.loggers.Event
import com.kafka.logger.loggers.EventInfo
import com.kafka.logger.loggers.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class LoggingInteractor<P : Event> {
    abstract val scope: CoroutineScope
    abstract val logger: Logger
    abstract val event: P

    operator fun invoke(params: suspend P.() -> EventInfo) {
        scope.launch { logger.log(params.invoke(event)) }
    }
}
