package com.kafka.user.config

import android.app.Application
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyController
import com.kafka.data.data.config.initializers.AppInitializer

class EpoxyInitializer : AppInitializer {
    override fun init(application: Application) {
        // Make EpoxyController async
        val handlerThread = EpoxyAsyncUtil.getAsyncBackgroundHandler()

        handlerThread.also {
            EpoxyController.defaultDiffingHandler = it
            EpoxyController.defaultModelBuildingHandler = it
        }

        EpoxyController.setGlobalDuplicateFilteringDefault(true)

        // Also setup Carousel to use a more sane snapping behavior
        Carousel.setDefaultGlobalSnapHelperFactory(null)

        EpoxyController.setGlobalDebugLoggingEnabled(true)
    }
}
