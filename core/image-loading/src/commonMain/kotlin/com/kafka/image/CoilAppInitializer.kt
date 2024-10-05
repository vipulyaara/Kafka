package com.kafka.image

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.util.DebugLogger
import com.kafka.base.AppInitializer
import me.tatarka.inject.annotations.Inject
import okio.FileSystem

class CoilAppInitializer @Inject constructor() : AppInitializer {
    override fun init() {
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(it)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .memoryCache {
                    MemoryCache.Builder().maxSizePercent(it, 0.3)
                        .strongReferencesEnabled(true)
                        .build()
                }
                .diskCachePolicy(CachePolicy.ENABLED)
                .diskCache {
                    DiskCache.Builder()
                        .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
                        .maxSizeBytes(512L * 1024 * 1024) // 512MB
                        .build()
                }
                .logger(DebugLogger())
                .build()
        }
    }
}
