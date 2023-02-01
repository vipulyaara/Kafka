package org.kafka.analytics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kafka.base.debug

interface Logger {
    fun log(eventInfo: EventInfo)
    fun updateUserProperty(update: UserData.() -> UserData)
    fun logScreenView(label: String, route: String?, arguments: Any?)
}

fun Logger.event(name: String, map: Map<String, Any> = emptyMap()) =
    log(EventInfo(name, map.mapValues { it.toString() }))

interface CrashLogger {
    fun initialize(userData: UserData)
    fun logFatal(throwable: Throwable)
    fun logNonFatal(throwable: Throwable)
}

data class UserData(val userId: String)

interface Event

typealias EventInfo = Pair<String, Map<String, String>>

abstract class LoggingInteractor<P> {
    abstract val scope: CoroutineScope
    abstract val logger: Logger
    abstract val event: P

    operator fun invoke(params: suspend P.() -> EventInfo) {
        scope.launch {
            val eventInfo = params.invoke(event)
            debug { "logging event $eventInfo" }
            logger.log(eventInfo)
        }
    }
}
