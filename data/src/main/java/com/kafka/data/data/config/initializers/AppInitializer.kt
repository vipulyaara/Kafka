package com.kafka.data.data.config.initializers

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}
