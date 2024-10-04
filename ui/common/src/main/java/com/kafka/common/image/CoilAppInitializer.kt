package com.kafka.common.image

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import com.kafka.base.AppInitializer
import com.kafka.base.CoroutineDispatchers
import java.io.File
import javax.inject.Inject

class CoilAppInitializer @Inject constructor(
    private val context: Application,
    private val dispatchers: CoroutineDispatchers,
) : AppInitializer {

    override fun init() {
        Coil.setImageLoader {
            ImageLoader.Builder(context)
                .dispatcher(dispatchers.io)
                .fetcherDispatcher(dispatchers.io)
                .diskCache(
                    DiskCache.Builder().directory(File(context.cacheDir, "images_cache")).build()
                )
                .build()
        }
    }
}
