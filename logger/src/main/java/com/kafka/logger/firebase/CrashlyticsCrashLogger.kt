package com.kafka.logger.firebase

import com.kafka.data.extensions.e
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.kafka.logger.loggers.CrashLogger
import dagger.Reusable
import javax.inject.Inject

@Reusable
class CrashlyticsCrashLogger @Inject constructor() : CrashLogger {
    override fun logNonFatal(throwable: Throwable) {
        // todo only throw in debug mode
        Firebase.crashlytics.recordException(throwable)
        e(throwable) { "" }
    }
}
