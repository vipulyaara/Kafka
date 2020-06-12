package com.kafka.user.injection

import android.content.Context
import androidx.startup.Initializer
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
