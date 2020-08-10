package com.kafka.logger.loggers

interface CrashLogger {
    fun logNonFatal(throwable: Throwable)
}
