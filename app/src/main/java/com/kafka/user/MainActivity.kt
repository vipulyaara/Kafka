package com.kafka.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kafka.data.prefs.PreferencesStore
import com.kafka.user.home.MainScreen
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

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            navController = rememberNavController(bottomSheetNavigator)
            AppTheme(
                isDarkTheme = preferencesStore.shouldUseDarkColors(),
                isTrueContrast = preferencesStore.shouldUseTrueContrast()
            ) {
                MainScreen(
                    navController = navController,
                    bottomSheetNavigator = bottomSheetNavigator
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (::navController.isInitialized) {
            navController.handleDeepLink(intent)
        } else {
            Timber.e(Error("navController is not initialized. isFinishing $isFinishing"))
        }
    }
}
