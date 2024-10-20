package com.kafka.analytics

import com.kafka.analytics.providers.AnalyticsProvider
import com.kafka.analytics.providers.EventInfo
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

actual interface AnalyticsPlatformComponent {
    @Provides
    @IntoSet
    fun provideFirebaseAnalytics(): AnalyticsProvider = object : AnalyticsProvider {
        override fun log(eventInfo: EventInfo) {
            // todo: kmp implement
        }

        override fun log(eventInfo: EventRepository.() -> EventInfo) {
            // todo: kmp implement
        }

        override fun logScreenView(label: String, route: String?, arguments: Any?) {
            // todo: kmp implement
        }
    }
}
