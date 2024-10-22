package com.kafka.common.platform

import android.app.Application
import android.content.Context
import com.kafka.base.ApplicationScope
import com.kafka.common.extensions.shareText
import me.tatarka.inject.annotations.Provides

actual interface CommonUiPlatformComponent {
    @ApplicationScope
    @Provides
    fun provideShareUtils(application: Application) = object : ShareUtils {
        override fun shareText(text: String, context: Any?) {
            (context as? Context)?.shareText(text)
        }
    }
}
