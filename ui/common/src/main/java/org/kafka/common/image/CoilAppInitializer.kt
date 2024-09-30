package org.kafka.common.image

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import okhttp3.OkHttpClient
import org.kafka.base.AppInitializer
import org.kafka.base.CoroutineDispatchers
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
