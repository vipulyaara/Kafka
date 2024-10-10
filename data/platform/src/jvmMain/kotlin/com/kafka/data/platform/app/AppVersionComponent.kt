package com.kafka.data.platform.app

import android.app.Application
import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides
import java.io.File

actual interface AppVersionComponent {
  @ApplicationScope
  @Provides
  fun provideAppVersionInfo(application: Application): AppVersionInfo {
      val properties = System.getProperty("java.class.path")
      val versionName = properties.split(File.separator).find { it.endsWith(".jar") }
          ?.let { File(it).nameWithoutExtension }

      return AppVersionInfo(versionName = versionName)
  }
}
