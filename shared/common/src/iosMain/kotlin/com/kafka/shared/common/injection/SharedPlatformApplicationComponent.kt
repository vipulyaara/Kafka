package com.kafka.shared.common.injection

import com.kafka.base.ApplicationInfo
import com.kafka.base.ApplicationScope
import com.kafka.base.Platform
import com.kafka.base.ProcessLifetime
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides
import platform.Foundation.NSBundle
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import kotlin.experimental.ExperimentalNativeApi

actual interface SharedPlatformApplicationComponent {

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @OptIn(ExperimentalNativeApi::class)
    @ApplicationScope
    @Provides
    fun provideApplicationId(): ApplicationInfo = ApplicationInfo(
        packageName = NSBundle.mainBundle.bundleIdentifier ?: error("Bundle ID not found"),
        debugBuild = kotlin.native.Platform.isDebugBinary,
        versionName = NSBundle.mainBundle.infoDictionary
            ?.get("CFBundleShortVersionString") as? String
            ?: "",
        versionCode = (NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as? String)
            ?.toIntOrNull()
            ?: 0,
        cachePath = { NSFileManager.defaultManager.cacheDir },
        platform = Platform.IOS,
    )
}

@OptIn(ExperimentalForeignApi::class)
private val NSFileManager.cacheDir: String
    get() = URLForDirectory(
        directory = NSCachesDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null,
    )?.path.orEmpty()
