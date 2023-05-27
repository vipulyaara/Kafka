package org.kafka.analytics

interface Analytics {
    fun log(eventInfo: EventInfo)
    fun log(eventInfo: EventRepository.() -> EventInfo)
    fun updateUserProperty(update: UserData.() -> UserData)
    fun logScreenView(label: String, route: String?, arguments: Any?)
}

fun Analytics.event(name: String, map: Map<String, Any> = emptyMap()) =
    log(EventInfo(name, map.mapValues { it.toString() }))

data class UserData(val userId: String)

typealias EventInfo = Pair<String, Map<String, String?>>
