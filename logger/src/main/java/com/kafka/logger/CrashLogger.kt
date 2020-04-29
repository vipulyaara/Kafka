package com.kafka.logger

interface CrashLogger {
    fun logNonFatal(throwable: Throwable)
}
