package org.kafka.analytics

import me.tatarka.inject.annotations.Provides
import org.kafka.analytics.logger.Analytics
import org.kafka.analytics.logger.AnalyticsImpl
import org.kafka.base.ApplicationScope

actual interface AnalyticsPlatformComponent {
  @ApplicationScope
  @Provides
  fun provideFirebaseAnalytics(bind: AnalyticsImpl): Analytics = bind
}
