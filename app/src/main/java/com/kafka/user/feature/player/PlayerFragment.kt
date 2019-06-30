package com.kafka.user.feature.player

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.entities.Item
import com.kafka.user.R
import com.kafka.user.config.NightModeManager
import com.kafka.user.databinding.FragmentHomeBinding
import com.kafka.user.feature.common.DataBindingMvRxFragment
import com.kafka.user.feature.home.HomepageController
import com.kafka.user.feature.home.HomepageViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author Vipul Kumar; dated 04/04/19.
 */
class PlayerFragment : DataBindingMvRxFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {

    private val viewModel: PlayerViewModel by fragmentViewModel()

    private val controller = PlayerController()

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
