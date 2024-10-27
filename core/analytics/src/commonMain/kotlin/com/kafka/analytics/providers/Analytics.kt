package com.kafka.analytics.providers

import com.kafka.analytics.EventRepository
import me.tatarka.inject.annotations.Inject

@Inject
class Analytics(val providers: Set<AnalyticsProvider>) {
    fun log(eventInfo: EventInfo) {
        providers.forEach { it.log(eventInfo) }
    }

    fun log(eventInfo: EventRepository.() -> EventInfo) {
        providers.forEach { it.log(eventInfo) }
    }

    fun logScreenView(label: String, route: String?, arguments: Any?) {
        providers.forEach { it.logScreenView(label, route, arguments) }
    }
}

interface AnalyticsProvider {
    fun log(eventInfo: EventInfo)
    fun log(eventInfo: EventRepository.() -> EventInfo)
    fun logScreenView(label: String, route: String?, arguments: Any?)
}

typealias EventInfo = Pair<String, Map<String, String?>>
