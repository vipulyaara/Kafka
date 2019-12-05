package org.rekhta.user.config.initializers

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}
