package com.kafka.user.feature

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kafka.user.R
import com.kafka.user.feature.common.BaseActivityMvRxView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivityMvRxView() {

    private lateinit var navController: NavController
    private var currentNavId = NAV_ID_NONE
    private var checkedItem = NAV_ID_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar?.apply {
            inflateMenu(R.menu.menu_master)
            navigationIcon = getDrawable(R.drawable.ic_data_usage_black_24dp)
        }

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentNavId = destination.id
        }

//        if (savedInstanceState == null) {
//            // default to showing Home
//            val initialNavId = intent.getIntExtra(EXTRA_NAVIGATION_ID, R.id.navigation_homepage)
//            checkedItem = initialNavId
//            navigateTo(initialNavId)
//        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        currentNavId = checkedItem
    }

    private fun navigateTo(navId: Int) {
        if (navId == currentNavId) {
            return // user tapped the current item
        }
        navController.navigate(navId)
    }

    override fun invalidate() {
    }

    companion object {
        /** Key for an int extra defining the initial navigation target. */
        const val EXTRA_NAVIGATION_ID = "extra.NAVIGATION_ID"

        private const val NAV_ID_NONE = -1

        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_homepage
        )
    }
}
