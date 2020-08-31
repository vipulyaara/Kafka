package com.kafka.user

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.kafka.content.ui.language.LanguageFragment
import com.kafka.content.ui.language.LanguageViewModel
import com.kafka.content.ui.main.MainFragment
import com.kafka.player.timber.permissions.PermissionsManager
import com.kafka.player.timber.playback.MusicService
import com.kafka.ui_common.extensions.setupToolbar
import com.kafka.ui_common.extensions.toggleNightMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var permissionsManager: PermissionsManager
    private val languageViewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(getColor(R.color.background)))
        setContentView(R.layout.activity_main)

        permissionsManager.attach(this)

        initToolbar()
        startMusicService()
        handleDeepLink()

        if (languageViewModel.areLanguagesSelected()) {
            supportFragmentManager.commit { replace(R.id.nav_host, MainFragment()) }
        } else {
            supportFragmentManager.commit {
                replace(R.id.nav_host, LanguageFragment())
                addToBackStack("")
            }
        }
    }

    private fun handleDeepLink() {
        val action: String? = intent?.action
        val data: Uri? = intent?.data
    }

    private fun startMusicService() {
        startService(Intent(this, MusicService::class.java))
    }

    private fun initToolbar() {
        toolbar?.setupToolbar(R.menu.menu_master) {
            when (it?.itemId) {
                R.id.menu_dark_mode -> toolbar?.toggleNightMode(this, it.itemId)
            }
        }
    }

    private fun shareItemLink() {

    }
}

