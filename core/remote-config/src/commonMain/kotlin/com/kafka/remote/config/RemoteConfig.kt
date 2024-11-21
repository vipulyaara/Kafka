package com.kafka.remote.config

import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
expect class RemoteConfig {

    fun get(key: String): String

    fun getBoolean(key: String): Boolean
}
