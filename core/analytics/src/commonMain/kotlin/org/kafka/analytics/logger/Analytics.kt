package org.kafka.analytics.logger

import org.kafka.analytics.EventRepository

interface Analytics {
    fun log(eventInfo: EventInfo)
    fun log(eventInfo: EventRepository.() -> EventInfo)
    fun updateUserProperty(userData: com.kafka.data.platform.UserData)
    fun logScreenView(label: String, route: String?, arguments: Any?)
}

typealias EventInfo = Pair<String, Map<String, String?>>
