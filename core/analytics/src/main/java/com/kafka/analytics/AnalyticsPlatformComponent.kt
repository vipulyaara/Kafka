package com.kafka.analytics

import me.tatarka.inject.annotations.Provides
import com.kafka.analytics.logger.Analytics
import com.kafka.analytics.logger.AnalyticsImpl
import com.kafka.base.ApplicationScope

actual interface AnalyticsPlatformComponent {
  @ApplicationScope
  @Provides
  fun provideFirebaseAnalytics(bind: AnalyticsImpl): Analytics = bind
}
