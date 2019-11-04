package com.kafka.user.feature

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import com.kafka.user.R
import com.kafka.user.feature.common.BaseActivity
import com.kafka.user.feature.home.NavigationViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val navigator by lazy {
        ViewModelProviders.of(this).get(NavigationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator.fragmentManager = supportFragmentManager

        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, MainFragment())
        }

        toolbar?.apply {
            inflateMenu(com.kafka.user.R.menu.menu_master)
            setOnMenuItemClickListener(navigator::onMenuItemClicked)
            navigationIcon = getDrawable(R.drawable.ic_data_usage_black_24dp)
        }
    }
}
