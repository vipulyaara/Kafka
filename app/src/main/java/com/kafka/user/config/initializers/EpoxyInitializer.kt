package org.rekhta.user.config.initializers

import android.app.Application
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyController
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 21/10/18.
 */
class EpoxyInitializer @Inject constructor() : AppInitializer {
    override fun init(application: Application) {
        // Make EpoxyController async
        val handlerThread = EpoxyAsyncUtil.getAsyncBackgroundHandler()

        handlerThread.also {
            EpoxyController.defaultDiffingHandler = it
        }

        EpoxyController.setGlobalDebugLoggingEnabled(true)

//        EpoxyController.setGlobalDuplicateFilteringDefault(true)

        // Also setup Carousel to use a more sane snapping behavior
        Carousel.setDefaultGlobalSnapHelperFactory(null)

        EpoxyController.setGlobalDebugLoggingEnabled(true)
    }
}
