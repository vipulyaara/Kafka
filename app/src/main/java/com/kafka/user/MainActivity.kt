package com.kafka.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.fragment.app.commit
import com.kafka.content.ui.main.MainFragment
import com.kafka.player.timber.permissions.PermissionsManager
import com.kafka.user.config.NightModeManager
import com.kafka.user.extensions.doOnEnd
import com.kafka.user.extensions.menuItemView
import com.kafka.user.extensions.springAnimation
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

        toolbar?.apply {
            inflateMenu(R.menu.menu_master)
            setOnMenuItemClickListener(menuItemListener)
        }

        supportFragmentManager.commit { replace(R.id.nav_host, MainFragment()) }
    }

    private val menuItemListener = Toolbar.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.menu_dark_mode -> {
                toolbar?.menuItemView(it.itemId)
                    .springAnimation(DynamicAnimation.ROTATION)
                    .doOnEnd { NightModeManager.toggleNightMode(this@MainActivity) }
                    .animateToFinalPosition(90f)
            }
        }
        true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
