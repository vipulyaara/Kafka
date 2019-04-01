package com.kafka.user.feature.profile

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.entities.Item
import com.kafka.user.R
import com.kafka.user.config.NightModeManager
import com.kafka.user.databinding.FragmentHomeBinding
import com.kafka.user.databinding.FragmentProfileBinding
import com.kafka.user.feature.common.DataBindingMvRxFragment
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 */

class ProfileFragment : DataBindingMvRxFragment<FragmentProfileBinding>(
    R.layout.fragment_profile
) {

    private val viewModel: ProfileViewModel by fragmentViewModel()

    private val controller = ProfileController(object : ProfileController.Callbacks {
        override fun onBookClicked(view: View, item: Item) {

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
