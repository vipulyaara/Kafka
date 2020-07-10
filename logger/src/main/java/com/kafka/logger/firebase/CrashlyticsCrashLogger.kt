package com.kafka.logger.firebase

import com.data.base.CrashLogger
import com.data.base.extensions.e
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashlyticsCrashLogger @Inject constructor() : CrashLogger {
    override fun logNonFatal(throwable: Throwable) {
        // todo only throw in debug mode after implementing crashlytics
        e(throwable) { "" }
    }
}
