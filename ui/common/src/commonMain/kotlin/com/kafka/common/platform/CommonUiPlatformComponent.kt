package com.kafka.common.platform

expect interface CommonUiPlatformComponent

interface ShareUtils {
    fun shareText(text: String, context: Any?)
}
