package com.airtel.data.config.initializers

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}
