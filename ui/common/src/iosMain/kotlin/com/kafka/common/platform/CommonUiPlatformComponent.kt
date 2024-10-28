package com.kafka.common.platform

import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides

actual interface CommonUiPlatformComponent {
    @ApplicationScope
    @Provides
    fun provideShareUtils() = object : ShareUtils {
        override fun shareText(text: String, context: Any?) {
            // todo: kmp implement this
        }
    }
}
