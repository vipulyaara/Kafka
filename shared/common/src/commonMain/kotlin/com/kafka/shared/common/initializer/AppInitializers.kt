package com.kafka.shared.common.initializer

import com.kafka.base.AppInitializer
import com.kafka.remote.config.RemoteConfig
import me.tatarka.inject.annotations.Inject

expect class LoggerInitializer : AppInitializer

expect class FirebaseInitializer : AppInitializer

@Inject
class RemoteConfigInitializer(
    private val remoteConfig: RemoteConfig,
) {
    fun init() {
        remoteConfig.init()
    }
}

@Inject
class AppInitializers(
    private val initializers: Set<AppInitializer>,
) {
    fun init() {
        initializers.forEach {
            it.init()
        }
    }
}
