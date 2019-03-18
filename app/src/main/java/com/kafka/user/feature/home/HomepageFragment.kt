package com.kafka.user.feature.home

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.entities.Item
import com.kafka.user.R
import com.kafka.user.config.NightModeManager
import com.kafka.user.databinding.FragmentHomeBinding
import com.kafka.user.feature.MainActivity
import com.kafka.user.feature.common.DataBindingMvRxFragment
import com.kafka.user.ui.SharedElementHelper
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 */

var detailId = ""
var detailName = ""
var detailUrl = ""

class HomepageFragment : DataBindingMvRxFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {

    private val viewModel: HomepageViewModel by fragmentViewModel()

    private val controller = HomepageController(object : HomepageController.Callbacks {
        override fun onBookClicked(view: View, item: Item) {
            detailId = item.itemId
            detailName = item.itemId
            detailUrl = item.coverImage ?: ""
            (activity as MainActivity).launchDetailFragment(
                this@HomepageFragment,
                SharedElementHelper().apply {
                    addSharedElement(view, view.transitionName)
                })
        }

        override fun onBannerClicked() {
            NightModeManager.toggleNightMode(activity)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHome.apply {
            setController(controller)
        }
    }

    override fun invalidate() {
        withState(viewModel) {
            controller.setData(it)
        }
    }
}
