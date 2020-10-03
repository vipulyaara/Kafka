package com.kafka.user

import android.os.Bundle
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.ui.platform.setContent
import androidx.fragment.app.FragmentActivity
import com.kafka.content.compose.MainWindow
import com.kafka.ui.ProvideDisplayInsets
import com.kafka.ui.theme.KafkaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityCompose : FragmentActivity() {
//    @Inject lateinit var permissionsManager: PermissionsManager

    @ExperimentalLazyDsl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        permissionsManager.attach(this)

        startMusicService()

        setCompose()
    }

    @ExperimentalLazyDsl
    private fun setCompose() {
        setContent {
            KafkaTheme {
                ProvideDisplayInsets {
                    MainWindow(onBackPressedDispatcher)
                }
            }
        }
    }

    private fun startMusicService() {
//        startService(Intent(this, MusicService::class.java))
    }
}

