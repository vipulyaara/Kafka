package com.kafka.user.injection

import android.content.Context
import androidx.startup.Initializer
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyController
import com.kafka.user.config.NightModeManager
import timber.log.Timber

class LoggerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.plant(Timber.DebugTree())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}

class ThemeInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        NightModeManager.apply {
            setCurrentNightMode(context, getCurrentMode(context))
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}

class EpoxyInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val asyncHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
        EpoxyController.defaultDiffingHandler = asyncHandler

        Carousel.setDefaultGlobalSnapHelperFactory(null)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
