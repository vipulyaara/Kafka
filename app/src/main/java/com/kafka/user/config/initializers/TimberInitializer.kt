package org.rekhta.user.config.initializers

import android.app.Application
import com.kafka.data.data.config.logging.Logger
import com.kafka.data.data.config.logging.TimberLogger
import com.kafka.user.BuildConfig
import javax.inject.Inject

class TimberInitializer @Inject constructor(private val timberLogger: Logger) : AppInitializer {
    override fun init(application: Application) {
        if (timberLogger is TimberLogger) {
            timberLogger.setup(BuildConfig.DEBUG)
        }
    }
}
