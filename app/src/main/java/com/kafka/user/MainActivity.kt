package com.kafka.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.kafka.content.ui.main.MainFragment
import com.kafka.player.timber.permissions.PermissionsManager
import com.kafka.ui_common.extensions.setupToolbar
import com.kafka.ui_common.extensions.toggleNightMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionsManager.attach(this)

        toolbar?.setupToolbar(R.menu.menu_master) {
            when (it?.itemId) {
                R.id.menu_dark_mode -> toolbar?.toggleNightMode(this, it.itemId)
            }
        }

        supportFragmentManager.commit { replace(R.id.nav_host, MainFragment()) }
    }
}

