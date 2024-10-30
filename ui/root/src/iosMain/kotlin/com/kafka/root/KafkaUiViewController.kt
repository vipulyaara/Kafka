package com.kafka.root

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.platform.AccessibilityDebugLogger
import androidx.compose.ui.platform.AccessibilitySyncOptions
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.kafka.base.ApplicationInfo
import com.kafka.data.prefs.Theme
import com.kafka.navigation.rememberBottomSheetNavigator
import me.tatarka.inject.annotations.Inject
import platform.UIKit.UIViewController

typealias KafkaUiViewController = () -> UIViewController

private const val ENABLE_A11Y_LOGGING = false

@OptIn(ExperimentalComposeApi::class)
@Inject
@Suppress("ktlint:standard:function-naming")
fun KafkaUiViewController(
  rootContent: RootContent,
  applicationInfo: ApplicationInfo,
): UIViewController = ComposeUIViewController(
  configure = {
    val a11yLogger = if (ENABLE_A11Y_LOGGING) {
      object : AccessibilityDebugLogger {
        override fun log(message: Any?) {
          Logger.d { "AccessibilityDebugLogger: $message" }
        }
      }
    } else {
      null
    }

    accessibilitySyncOptions = when {
      applicationInfo.debugBuild -> AccessibilitySyncOptions.Always(a11yLogger)
      else -> AccessibilitySyncOptions.WhenRequiredByAccessibilityServices(a11yLogger)
    }
  },
) {
  val bottomSheetNavigator = rememberBottomSheetNavigator()
  val navController = rememberNavController(bottomSheetNavigator)

  rootContent.Content(navController, bottomSheetNavigator, Theme.SYSTEM)
}
