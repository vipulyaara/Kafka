package com.kafka.user

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.kafka.analytics.Logger
import org.kafka.common.extensions.rememberFlowStateWithLifecycle
import ui.common.theme.ThemeViewModel
import ui.common.theme.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    internal lateinit var analytics: Logger

    @Inject
    lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        permissionsManager.attach(this)
        startMusicService()

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val dynamicColor by rememberFlowStateWithLifecycle(
                flow = themeViewModel.themeState,
                default = true
            )

            AppTheme(dynamicColor) {
                MainScreen(analytics = analytics)
            }
        }
    }

    private fun startMusicService() {
//        startService(Intent(this, MusicService::class.java))
    }
}
