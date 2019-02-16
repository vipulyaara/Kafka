package com.airtel.kafkapp.feature

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.transaction
import com.airtel.kafkapp.R
import com.airtel.kafkapp.feature.detail.ItemDetailFragment
import com.airtel.kafkapp.feature.search.SearchFragment
import com.airtel.kafkapp.ui.widget.TabItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchMainFragment()

        toolbar.apply {
            inflateMenu(R.menu.menu_book_detail)
            setOnMenuItemClickListener(this@MainActivity::onMenuItemClicked)
        }

        setSupportActionBar(toolbar).also { title = "" }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_book_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun launchMainFragment() {
        supportFragmentManager.transaction {
            replace(R.id.fragmentContainer, MainFragment())
        }
    }

    fun launchSearchFragment() {
        supportFragmentManager.transaction {
            replace(R.id.fragmentContainer, SearchFragment())
            addToBackStack("")
        }
    }

    fun launchDetailFragment() {
        supportFragmentManager.transaction {
            addToBackStack("")
            add(
                R.id.fragmentContainer,
                ItemDetailFragment.create("metamorphosis_librivox")
            )
        }
    }

    private fun onMenuItemClicked(item: MenuItem) = when (item.itemId) {
        R.id.menu_share -> {
            launchSearchFragment()
            true
        }
        else -> false
    }
}
