package com.kafka.shared.common.injection

import android.app.Application
import android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.kafka.base.ApplicationInfo
import com.kafka.base.ApplicationScope
import com.kafka.base.Platform
import com.kafka.base.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Provides

actual interface SharedPlatformApplicationComponent {

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycleScope
    }

    @ApplicationScope
    @Provides
    fun provideApplicationInfo(
        application: Application
    ): ApplicationInfo {
        val packageManager = application.packageManager
        val applicationInfo = packageManager.getApplicationInfo(application.packageName, 0)
        val packageInfo = packageManager.getPackageInfo(application.packageName, 0)

        return ApplicationInfo(
            packageName = application.packageName,
            debugBuild = (applicationInfo.flags and FLAG_DEBUGGABLE) != 0,
            versionName = packageInfo.versionName.orEmpty(),
            versionCode = @Suppress("DEPRECATION") packageInfo.versionCode,
            cachePath = { application.cacheDir.absolutePath },
            platform = Platform.ANDROID,
        )
    }
}
