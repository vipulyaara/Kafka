package com.kafka.shared.injection

import com.kafka.base.ApplicationInfo
import com.kafka.base.ApplicationScope
import com.kafka.base.Platform
import com.kafka.base.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides
import java.io.File

actual interface SharedPlatformApplicationComponent {

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @ApplicationScope
    @Provides
    fun provideApplicationId(): ApplicationInfo = ApplicationInfo(
        packageName = "com.kafka.user",
        debugBuild = true,
        versionName = "0.0.9",
        versionCode = 90,
        cachePath = { getCacheDir().absolutePath },
        platform = Platform.DESKTOP,
    )
}

private fun getCacheDir(): File = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "tivi/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/tivi")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/tivi")
    else -> throw IllegalStateException("Unsupported operating system")
}

internal enum class OperatingSystem {
    Windows,
    Linux,
    MacOS,
    Unknown,
}

private val currentOperatingSystem: OperatingSystem
    get() {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("win") -> OperatingSystem.Windows
            os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                OperatingSystem.Linux
            }

            os.contains("mac") -> OperatingSystem.MacOS
            else -> OperatingSystem.Unknown
        }
    }
