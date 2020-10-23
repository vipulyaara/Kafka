package com.kafka.user

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import androidx.fragment.app.FragmentActivity
import com.kafka.content.compose.main.MainWindow
import com.kafka.player.timber.playback.MusicService
import com.kafka.ui.theme.SysUiController
import com.kafka.ui.theme.SystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeMainActivity : FragmentActivity() {

    @ExperimentalMaterialApi
    @ExperimentalLazyDsl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMusicService()

        setContent {
            val systemUiController = remember { SystemUiController(window) }
            Providers(SysUiController provides systemUiController) {
                MainWindow(onBackPressedDispatcher)
            }
        }
    }

    private fun startMusicService() {
        startService(Intent(this, MusicService::class.java))
    }
}

