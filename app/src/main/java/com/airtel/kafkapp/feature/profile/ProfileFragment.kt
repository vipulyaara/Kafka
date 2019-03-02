package com.airtel.kafkapp.feature.profile

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.airtel.data.entities.Item
import com.airtel.kafkapp.R
import com.airtel.kafkapp.config.NightModeManager
import com.airtel.kafkapp.databinding.FragmentHomeBinding
import com.airtel.kafkapp.feature.MainActivity
import com.airtel.kafkapp.feature.common.DataBindingMvRxFragment
import com.airtel.kafkapp.feature.home.HomepageController
import com.airtel.kafkapp.feature.home.HomepageViewModel
import com.airtel.kafkapp.feature.home.detailId
import com.airtel.kafkapp.feature.home.detailName
import com.airtel.kafkapp.feature.home.detailUrl
import com.airtel.kafkapp.ui.SharedElementHelper
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author Vipul Kumar; dated 02/02/19.
 */

class ProfileFragment : DataBindingMvRxFragment<FragmentHomeBinding>(
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
