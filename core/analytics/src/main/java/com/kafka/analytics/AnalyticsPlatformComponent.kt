package com.kafka.analytics

import com.kafka.analytics.providers.AnalyticsProvider
import com.kafka.analytics.providers.FirebaseAnalytics
import com.kafka.analytics.providers.MixpanelAnalytics
import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

actual interface AnalyticsPlatformComponent {
    @ApplicationScope
    @Provides
    @IntoSet
    fun provideFirebaseAnalytics(bind: FirebaseAnalytics): AnalyticsProvider = bind

    @ApplicationScope
    @Provides
    @IntoSet
    fun provideMixpanelAnaytics(bind: MixpanelAnalytics): AnalyticsProvider = bind
}
