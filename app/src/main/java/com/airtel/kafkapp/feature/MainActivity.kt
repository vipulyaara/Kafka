package com.airtel.kafkapp.feature

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.airtel.kafkapp.R
import com.airtel.kafkapp.feature.detail.ItemDetailFragment
import com.airtel.kafkapp.feature.home.HomeFragment
import com.airtel.kafkapp.feature.search.SearchFragment
import com.airtel.kafkapp.ui.widget.TabItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchHomeFragment()

        toolbar.setNavigationOnClickListener { launchSearchFragment() }

        setSupportActionBar(toolbar).also { title = "" }

        val icons = arrayOf(
            R.drawable.ic_home,
            R.drawable.ic_explore,
            R.drawable.ic_library,
            R.drawable.ic_library,
            R.drawable.ic_library
        )
        repeat(5) { bottomBar.addItem(TabItem(icons[it])) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_book_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun launchSearchFragment() {
        supportFragmentManager.transaction {
            add(R.id.fragmentContainer, SearchFragment())
            addToBackStack("")
        }
    }

    fun launchHomeFragment() {
        supportFragmentManager.transaction {
            replace(R.id.fragmentContainer, HomeFragment())
        }
    }

    fun launchDetailFragment() {
        supportFragmentManager.transaction {
            addToBackStack("")
            replace(
                R.id.fragmentContainer,
                ItemDetailFragment().apply { arguments = Bundle().apply { putString("itemId", "metamorphosis_librivox") } })
        }
    }
}
