package com.airtel.kafkapp.feature

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.transaction
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.TransitionSet
import com.airtel.kafkapp.DetailActivity
import com.airtel.kafkapp.R
import com.airtel.kafkapp.extensions.logger
import com.airtel.kafkapp.feature.common.BaseActivity
import com.airtel.kafkapp.feature.detail.ItemDetailFragment
import com.airtel.kafkapp.feature.home.detailName
import com.airtel.kafkapp.feature.search.SearchFragment
import com.airtel.kafkapp.ui.SharedElementHelper
import io.reactivex.internal.util.BackpressureHelper.add

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchMainFragment()


        toolbar = findViewById<Toolbar>(R.id.toolbar).also { title = "" }
        setSupportActionBar(toolbar)
        toolbar?.apply {
            inflateMenu(R.menu.menu_master)
            setOnMenuItemClickListener(this@MainActivity::onMenuItemClicked)
        }
    }

    private fun launchMainFragment() {
        supportFragmentManager.transaction {
            replace(R.id.fragmentContainer, MainFragment())
        }
    }

    private fun launchSearchFragment() {
        supportFragmentManager.transaction {
            add(R.id.fragmentContainer, SearchFragment())
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
            add(R.id.fragmentContainer, fragment)
        }
    }

    fun launchDetailFragment(sharedElements: SharedElementHelper?) {
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent, sharedElements?.applyToIntent(this) )
    }

    private fun onMenuItemClicked(item: MenuItem) = when (item.itemId) {
        R.id.menu_search -> {
            launchSearchFragment()
            true
        }
        else -> false
    }
}
