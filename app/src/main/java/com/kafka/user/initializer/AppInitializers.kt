package com.kafka.user.initializer

import android.app.Application
import com.kafka.data.AppInitializer
import javax.inject.Inject

class AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer>
) {
    fun init(application: Application) {
        initializers.forEach {
            it.init(application)
        }
    }
}
