package com.kafka.data.platform.app

import android.app.Application
import android.content.pm.PackageManager
import com.kafka.base.ApplicationScope
import com.kafka.base.errorLog
import me.tatarka.inject.annotations.Provides

actual interface AppVersionComponent {
  @ApplicationScope
  @Provides
  fun provideAppVersionInfo(application: Application): AppVersionInfo {
      val packageInfo =  try {
        application.packageManager.getPackageInfo(application.packageName, 0)
      } catch (e: PackageManager.NameNotFoundException) {
        errorLog { "Unable to get version name" }
        null
      }

      return AppVersionInfo(versionName = packageInfo?.versionName)
  }
}
