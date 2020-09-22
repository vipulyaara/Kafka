package com.kafka.user

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.ui.platform.setContent
import com.kafka.content.compose.MainWindow
import com.kafka.player.timber.permissions.PermissionsManager
import com.kafka.player.timber.playback.MusicService
import com.kafka.ui.ProvideDisplayInsets
import com.kafka.ui.theme.KafkaTheme
import com.kafka.ui_common.extensions.setupToolbar
import com.kafka.ui_common.extensions.toggleNightMode
import com.kafka.ui_common.extensions.viewBinding
import com.kafka.user.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    @Inject lateinit var permissionsManager: PermissionsManager

    @ExperimentalLazyDsl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(getColor(R.color.background)))
        setContentView(R.layout.activity_main)

        permissionsManager.attach(this)

//        initToolbar()
        startMusicService()
        handleDeepLink()

        setContent {
            KafkaTheme {
                ProvideDisplayInsets {
                    MainWindow(onBackPressedDispatcher)
                }
            }
        }

//        supportFragmentManager.commit { replace(R.id.nav_host, MainFragment()) }

    }

    private fun handleDeepLink() {
        val action: String? = intent?.action
        val data: Uri? = intent?.data
    }

    private fun startMusicService() {
        startService(Intent(this, MusicService::class.java))
    }

    private fun initToolbar() {
        binding.toolbar.setupToolbar(R.menu.menu_master) {
            when (it?.itemId) {
                R.id.menu_dark_mode -> binding.toolbar.toggleNightMode(this, it.itemId)
            }
        }
    }

    private fun shareItemLink() {

    }
}

