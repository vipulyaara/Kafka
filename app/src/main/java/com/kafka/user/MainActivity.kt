package com.kafka.user

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
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme
import com.kafka.user.home.MainScreen
import com.sarahang.playback.ui.color.ColorExtractor
import dagger.hilt.android.AndroidEntryPoint
import org.kafka.base.errorLog
import org.kafka.navigation.rememberBottomSheetNavigator
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.shouldUseDarkColors
import ui.common.theme.theme.shouldUseTrueContrast
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    internal lateinit var preferencesStore: PreferencesStore

    @Inject
    internal lateinit var colorExtractor: ColorExtractor

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val theme by preferencesStore.observeTheme().collectAsStateWithLifecycle(Theme.SYSTEM)
            val isDarkTheme = preferencesStore.shouldUseDarkColors()
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            navController = rememberNavController(bottomSheetNavigator)

            DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(isDarkTheme)
                onDispose {}
            }

            AppTheme(
                isDarkTheme = preferencesStore.shouldUseDarkColors(),
                isTrueContrast = preferencesStore.shouldUseTrueContrast()
            ) {
                MainScreen(
                    navController = navController,
                    colorExtractor = colorExtractor,
                    bottomSheetNavigator = bottomSheetNavigator,
                    theme = theme
                )
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
