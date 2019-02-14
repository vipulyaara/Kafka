package com.airtel.data.data.config.logging

import android.app.Application
import com.airtel.data.BuildConfig
import com.airtel.data.data.config.initializers.AppInitializer

class TimberInitializer constructor(
    private val timberLogger: Logger
) : AppInitializer {
    override fun init(application: Application) {
        if (timberLogger is TimberLogger) {
            timberLogger.setup(BuildConfig.DEBUG)
        }
    }
}
