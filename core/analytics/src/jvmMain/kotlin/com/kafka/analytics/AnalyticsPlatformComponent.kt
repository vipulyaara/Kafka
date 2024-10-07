package com.kafka.analytics

import com.kafka.analytics.logger.Analytics
import com.kafka.analytics.logger.EventInfo
import com.kafka.data.platform.UserData
import me.tatarka.inject.annotations.Provides

actual interface AnalyticsPlatformComponent {
  @Provides
  fun provideFirebaseAnalytics(): Analytics = object : Analytics {
    override fun log(eventInfo: EventInfo) {
      // todo: kmp implement
    }

    override fun log(eventInfo: EventRepository.() -> EventInfo) {
      // todo: kmp implement
    }

    override fun updateUserProperty(userData: UserData) {
      // todo: kmp implement
    }

    override fun logScreenView(label: String, route: String?, arguments: Any?) {
      // todo: kmp implement
    }

  }
}
