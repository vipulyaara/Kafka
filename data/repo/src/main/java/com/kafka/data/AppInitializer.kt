package com.kafka.data

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}
