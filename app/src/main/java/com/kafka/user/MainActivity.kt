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
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.shouldUseDarkColors
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
            AppTheme(isDarkTheme = preferencesStore.shouldUseDarkColors()) {
                MainScreen(
                    navController = navController,
                    bottomSheetNavigator = bottomSheetNavigator
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}
