package com.airtel.kafkapp.feature

import android.os.Bundle
import android.view.View
import androidx.fragment.app.transaction
import com.airtel.kafkapp.R
import com.airtel.kafkapp.feature.common.DataBindingFragment
import com.airtel.kafkapp.feature.home.HomepageFragment
import com.airtel.kafkapp.ui.widget.TabItem
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @author Vipul Kumar; dated 16/02/19.
 */
class MainFragment : DataBindingFragment<com.airtel.kafkapp.databinding.FragmentMainBinding>(
    R.layout.fragment_main
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchHomeFragment()

        val icons = arrayOf(
            TabItem(R.drawable.ic_book),
            TabItem(R.drawable.ic_explore),
            TabItem(R.drawable.ic_bookmark, true),
            TabItem(R.drawable.ic_library)
        )

        repeat(icons.size) { bottomBar.addItem(icons[it]) }
    }

    private fun launchHomeFragment() {
        childFragmentManager.transaction {
            replace(R.id.fragmentContainer, HomepageFragment())
        }
    }
}
