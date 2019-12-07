package com.kafka.user.config.initializers

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 21/10/18.
 */
class ThemeInitializer @Inject constructor() : AppInitializer {
    override fun init(application: Application) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
