package org.rekhta.user.config.initializers

import android.app.Application
import com.facebook.stetho.Stetho
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StethoInitializer @Inject constructor() : AppInitializer {
    override fun init(application: Application)  {
        GlobalScope.launch {
            Stetho.initializeWithDefaults(application)
        }
    }
}
