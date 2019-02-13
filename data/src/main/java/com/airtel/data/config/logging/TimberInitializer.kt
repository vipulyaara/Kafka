package com.airtel.data.config.logging

import android.app.Application
import com.airtel.data.BuildConfig
import com.airtel.data.config.initializers.AppInitializer
import com.airtel.data.config.logging.Logger
import com.airtel.data.config.logging.TimberLogger

class TimberInitializer constructor(
    private val timberLogger: Logger
) : AppInitializer {
    override fun init(application: Application) {
        if (timberLogger is TimberLogger) {
            timberLogger.setup(BuildConfig.DEBUG)
        }
    }
}
