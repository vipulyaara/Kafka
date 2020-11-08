package com.kafka.user

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.fragment.app.commit
import com.kafka.content.ui.main.MainFragment
import com.kafka.player.timber.permissions.PermissionsManager
import com.kafka.player.timber.playback.MusicService
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
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )

        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(getColor(R.color.background)))
        setContentView(R.layout.activity_main)

        permissionsManager.attach(this)

        initToolbar()
        startMusicService()



        supportFragmentManager.commit { replace(R.id.nav_host, MainFragment()) }
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

