package com.kafka.shared.common.initializer

import com.kafka.base.AppInitializer
import me.tatarka.inject.annotations.Inject

expect class LoggerInitializer : AppInitializer

expect class FirebaseInitializer : AppInitializer

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
