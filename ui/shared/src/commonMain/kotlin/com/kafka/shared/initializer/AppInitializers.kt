package com.kafka.shared.initializer

import com.kafka.base.AppInitializer
import com.kafka.remote.config.RemoteConfig
import javax.inject.Inject

expect class LoggerInitializer : AppInitializer

expect class FirebaseInitializer : AppInitializer

class RemoteConfigInitializer @Inject constructor(
    private val remoteConfig: RemoteConfig,
) {
    fun init() {
        remoteConfig.init()
    }
}

class AppInitializers @Inject constructor(
    private val initializers: Set<AppInitializer>,
) {
    fun init() {
        initializers.forEach {
            it.init()
        }
    }
}
