package com.kafka.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.kafka.user.home.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import org.kafka.navigation.deeplink.DynamicDeepLinkHandler
import ui.common.theme.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    internal lateinit var dynamicLinkHandler: DynamicDeepLinkHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        dynamicLinkHandler.handleDeepLink(this, intent!!)
    }
}
