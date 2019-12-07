package com.kafka.user.feature.home

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.entities.Content
import com.kafka.user.R
import com.kafka.user.config.NightModeManager
import com.kafka.user.databinding.FragmentHomeBinding
import com.kafka.user.feature.common.BaseDataBindingFragment
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 02/02/19.
 */

class HomepageFragment : BaseDataBindingFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {

    @Inject
    lateinit var discoverViewModelFactory: HomepageViewModel.Factory

    @Inject
    lateinit var controller: HomepageController

    private val viewModel: HomepageViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHome.apply {
            setController(controller)
        }

        controller.callbacks = object : HomepageController.Callbacks {
            override fun onBookClicked(view: View, content: Content) {
//                detailId = item.contentId
//                detailName = item.contentId
//                detailUrl = item.coverImage ?: ""
//                navigator.showItemDetail(
//                    item,
//                    SharedElementHelper().apply {
//                        addSharedElement(view, ViewCompat.getTransitionName(view))
//                    })
            }

            override fun onBannerClicked() {
                NightModeManager.toggleNightMode(activity)
            }
        }
    }

    override fun invalidate() {
        withState(viewModel) {
            controller.state = it
        }
    }
}
