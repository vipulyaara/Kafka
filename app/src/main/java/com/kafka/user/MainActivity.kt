package com.kafka.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kafka.player.timber.permissions.PermissionsManager
import com.kafka.user.config.NightModeManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    @Inject lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionsManager.attach(this)

        toolbar?.apply {
            inflateMenu(R.menu.menu_master)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menud_dark_mode -> { NightModeManager.toggleNightMode(this@MainActivity) }
                    else -> { }
                }
                true
            }
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_menu)
        }

        navController = findNavController(R.id.nav_host_fragment)
    }
}
