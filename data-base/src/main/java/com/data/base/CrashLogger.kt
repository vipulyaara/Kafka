package com.data.base

import java.lang.Exception

interface CrashLogger {
    fun logNonFatal(throwable: Throwable)
}
