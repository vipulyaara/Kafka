package com.kafka.user.feature

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.transaction
import com.kafka.user.DetailActivity
import com.kafka.user.R
import com.kafka.user.extensions.logger
import com.kafka.user.feature.common.BaseActivity
import com.kafka.user.feature.detail.ItemDetailFragment
import com.kafka.user.feature.search.SearchFragment
import com.kafka.user.ui.SharedElementHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kafka.user.R.layout.activity_main)

        launchMainFragment()

        toolbar?.apply {
            inflateMenu(com.kafka.user.R.menu.menu_master)
            setOnMenuItemClickListener(this@MainActivity::onMenuItemClicked)
            navigationIcon = getDrawable(R.drawable.ic_data_usage_black_24dp)
        }
    }

    private fun launchMainFragment() {
        supportFragmentManager.transaction {
            replace(com.kafka.user.R.id.fragmentContainer, MainFragment())
        }
    }

    private fun launchSearchFragment() {
        supportFragmentManager.transaction {
            add(com.kafka.user.R.id.fragmentContainer, SearchFragment())
            addToBackStack("")
        }
    }

    fun launchDetailFragment(fragmentsss: Fragment, sharedElements: SharedElementHelper?) {
        val fragment = ItemDetailFragment.create()
        supportFragmentManager?.transaction {
            addToBackStack("")
            apply {
                if (sharedElements != null && !sharedElements.isEmpty()) {
                    logger.d("shared element is ${sharedElements.sharedElementViews}")
                            sharedElements.applyToTransaction(this)
                } else {
                    logger.d("shared element is null $sharedElements")
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                }
            }
            detach(fragmentsss)
            add(com.kafka.user.R.id.fragmentContainer, fragment)
        }
    }

    fun launchDetailFragment(sharedElements: SharedElementHelper?) {
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent, sharedElements?.applyToIntent(this) )
    }

    private fun onMenuItemClicked(item: MenuItem) = when (item.itemId) {
        com.kafka.user.R.id.menu_search -> {
            launchSearchFragment()
            true
        }
        else -> false
    }
}
