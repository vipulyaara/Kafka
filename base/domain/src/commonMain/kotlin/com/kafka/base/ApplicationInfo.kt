package com.kafka.base

data class ApplicationInfo(
  val packageName: String,
  val debugBuild: Boolean,
  val versionName: String,
  val versionCode: Int,
  val cachePath: () -> String,
  val platform: Platform,
)

enum class Platform {
  IOS,
  ANDROID,
  DESKTOP,
}
