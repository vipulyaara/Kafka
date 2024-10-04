package com.kafka.analytics.logger

import com.kafka.data.platform.UserData
import com.kafka.analytics.EventRepository

interface Analytics {
    fun log(eventInfo: EventInfo)
    fun log(eventInfo: EventRepository.() -> EventInfo)
    fun updateUserProperty(userData: UserData)
    fun logScreenView(label: String, route: String?, arguments: Any?)
}

typealias EventInfo = Pair<String, Map<String, String?>>
