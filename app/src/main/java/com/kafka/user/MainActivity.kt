package com.kafka.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.kafka.user.home.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import org.kafka.analytics.Analytics
import org.kafka.navigation.DynamicDeepLinkHandler
import ui.common.theme.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    internal lateinit var analytics: Analytics

    @Inject
    internal lateinit var dynamicLinkHandler: DynamicDeepLinkHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                MainScreen(analytics = analytics)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        dynamicLinkHandler.handleDeepLink(this, intent!!)
    }
}
