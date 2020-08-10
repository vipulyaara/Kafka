package com.kafka.logger.firebase

import com.data.base.extensions.e
import com.kafka.logger.loggers.CrashLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashlyticsCrashLogger @Inject constructor() : CrashLogger {
    override fun logNonFatal(throwable: Throwable) {
        // todo only throw in debug mode after implementing crashlytics
        e(throwable) { "" }
    }
}
