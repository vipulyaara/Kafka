package com.kafka.shared.common.initializer

import com.kafka.analytics.providers.Analytics
import com.kafka.base.AppInitializer
import com.kafka.base.ApplicationScope
import com.kafka.remote.config.RECOMMENDATION_ROW_ENABLED
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isRecommendationRowEnabled
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class RemoteConfigLogger(
    private val remoteConfig: RemoteConfig,
    private val analytics: Analytics,
) : AppInitializer {
    override fun init() {
        analytics.log {
            remoteConfigValue(
                key = RECOMMENDATION_ROW_ENABLED,
                value = remoteConfig.isRecommendationRowEnabled()
            )
        }
    }
}
