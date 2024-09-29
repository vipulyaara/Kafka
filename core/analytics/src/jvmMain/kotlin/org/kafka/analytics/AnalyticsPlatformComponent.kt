package org.kafka.analytics

import com.kafka.data.platform.UserData
import me.tatarka.inject.annotations.Provides
import org.kafka.analytics.logger.Analytics
import org.kafka.analytics.logger.EventInfo

actual interface AnalyticsPlatformComponent {
  @Provides
  fun provideFirebaseAnalytics(): Analytics = object : Analytics {
    override fun log(eventInfo: EventInfo) {
      TODO("Not yet implemented")
    }

    override fun log(eventInfo: EventRepository.() -> EventInfo) {
      TODO("Not yet implemented")
    }

    override fun updateUserProperty(userData: UserData) {
      TODO("Not yet implemented")
    }

    override fun logScreenView(label: String, route: String?, arguments: Any?) {
      TODO("Not yet implemented")
    }

  }
}
