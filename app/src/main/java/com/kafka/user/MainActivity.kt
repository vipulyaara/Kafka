package com.kafka.user

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kafka.base.errorLog
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme
import com.kafka.navigation.rememberBottomSheetNavigator
import com.kafka.remote.config.isTrueContrastEnabled
import com.kafka.shared.injection.AndroidActivityComponent
import com.kafka.shared.injection.AndroidApplicationComponent
import com.kafka.shared.injection.create
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.shouldUseDarkColors

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val applicationComponent = AndroidApplicationComponent.from(this)
        val component = AndroidActivityComponent::class.create(this, applicationComponent)

        setContent {
            val theme by applicationComponent.preferencesStore.observeTheme()
                .collectAsStateWithLifecycle(Theme.SYSTEM)
            val isDarkTheme = applicationComponent.preferencesStore.shouldUseDarkColors()
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            navController = rememberNavController(bottomSheetNavigator)

            DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(isDarkTheme)
                onDispose {}
            }

            AppTheme(
                isDarkTheme = applicationComponent.preferencesStore.shouldUseDarkColors(),
                isTrueContrast = applicationComponent.remoteConfig.isTrueContrastEnabled()
            ) {
                component.mainScreen(navController, bottomSheetNavigator, theme)
            }
        }
    }

    private fun enableEdgeToEdge(isDarkTheme: Boolean) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ) { isDarkTheme },
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim,
                darkScrim,
            ) { isDarkTheme },
        )
    }

    @Suppress("SENSELESS_COMPARISON") // intent is somehow null
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (::navController.isInitialized && intent != null) {
            navController.handleDeepLink(intent)
        } else {
            errorLog(Error("navController is not initialized or intent is null. isFinishing = $isFinishing, intent = $intent"))
        }
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)

fun AndroidApplicationComponent.Companion.from(context: Context): AndroidApplicationComponent {
    return (context.applicationContext as KafkaApplication).component
}
