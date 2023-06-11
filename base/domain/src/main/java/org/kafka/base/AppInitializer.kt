package org.kafka.base

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}
