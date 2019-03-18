package com.kafka.data.data.config.logging

import android.app.Application
import com.kafka.data.BuildConfig
import com.kafka.data.data.config.initializers.AppInitializer

class TimberInitializer constructor(
    private val timberLogger: Logger
) : AppInitializer {
    override fun init(application: Application) {
        if (timberLogger is TimberLogger) {
            timberLogger.setup(BuildConfig.DEBUG)
        }
    }
}
