package com.airtel.data.config.initializers

import android.app.Application
import com.airtel.data.config.kodeinInstance
import org.kodein.di.generic.instance

class AppInitializers {
    private val initializers: MutableSet<@JvmSuppressWildcards AppInitializer> = hashSetOf()

    init {
        val timber: AppInitializer by kodeinInstance.instance("TimberInitializer")
        initializers.add(timber)
    }

    fun init(application: Application) {
        initializers.forEach {
            it.init(application)
        }
    }
}
