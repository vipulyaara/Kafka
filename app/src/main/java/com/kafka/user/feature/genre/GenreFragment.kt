package com.kafka.user.feature.genre

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.entities.Item
import com.kafka.user.R
import com.kafka.user.config.NightModeManager
import com.kafka.user.databinding.FragmentHomeBinding
import com.kafka.user.feature.common.DataBindingMvRxFragment
import com.kafka.user.feature.profile.ProfileController
import com.kafka.user.feature.profile.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 */

class GenreFragment : DataBindingMvRxFragment<FragmentHomeBinding>(
    R.layout.fragment_profile
) {

    private val viewModel: GenreViewModel by fragmentViewModel()

    private val controller = GenreController(object : GenreController.Callbacks {
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
