package com.kafka.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    @SerialName("app_update") val appUpdate: AppUpdateConfig,
)

@Serializable
data class AppUpdateConfig(
    @SerialName("soft_update_version") val softUpdateVersion: Int,
    @SerialName("force_update_version") val forceUpdateVersion: Int,
    @SerialName("blocked_update_version") val blockedAppVersions: List<Int>,
)
