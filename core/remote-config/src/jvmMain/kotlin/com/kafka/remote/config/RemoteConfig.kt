package com.kafka.remote.config

import com.kafka.base.ApplicationScope
import javax.inject.Inject

@ApplicationScope
actual class RemoteConfig @Inject constructor() {

    actual fun get(key: String): String = ""

    actual fun getBoolean(key: String): Boolean = false

    actual fun getLong(key: String): Long = 0L
}
