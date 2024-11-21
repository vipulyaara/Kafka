package com.kafka.remote.config

import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
actual class RemoteConfig {
    actual fun get(key: String): String = ""

    actual fun getBoolean(key: String): Boolean = false

}