package com.kafka.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme
import com.kafka.user.home.MainScreen
import com.sarahang.playback.ui.color.ColorExtractor
import dagger.hilt.android.AndroidEntryPoint
import org.kafka.navigation.rememberBottomSheetNavigator
import timber.log.Timber
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
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val theme by preferencesStore.observeTheme().collectAsStateWithLifecycle(Theme.SYSTEM)
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            navController = rememberNavController(bottomSheetNavigator)

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (::navController.isInitialized && intent != null) { // intent is somehow null
            navController.handleDeepLink(intent)
        } else {
            Timber.e(Error("navController is not initialized or intent is null. isFinishing = $isFinishing, intent = $intent"))
        }
    }
}
