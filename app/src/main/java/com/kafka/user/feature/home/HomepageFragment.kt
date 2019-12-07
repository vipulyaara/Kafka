package com.kafka.user.feature.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.model.EventObserver
import com.kafka.user.R
import com.kafka.user.databinding.FragmentHomeBinding
import com.kafka.user.feature.common.BaseDataBindingFragment
import com.kafka.user.feature.home.HomepageFragmentDirections.Companion.toPoetDetail
import com.kafka.user.ui.onScrolled
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
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHome.apply {
            setController(controller)
            itemAnimator = null
            onScrolled {
                binding.elevationShadow.isActivated = canScrollVertically(-1)
            }
        }

//        controller.callbacks = object : HomepageController.Callbacks {
//            override fun onContentClicked(view: View, content: Content) {
////                detailId = item.contentId
////                detailName = item.contentId
////                detailUrl = item.coverImage ?: ""
////                navigator.showItemDetail(
////                    item,
////                    SharedElementHelper().apply {
////                        addSharedElement(view, ViewCompat.getTransitionName(view))
////                    })
//            }
//
//            override fun onBannerClicked() {
//                NightModeManager.toggleNightMode(activity)
//            }
//        }

        viewModel.navigateToContentDetailAction.observe(this,
            EventObserver { content -> navController.navigate(toPoetDetail(content.contentId)) }
        )

        controller.callbacks = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun invalidate() {
        withState(viewModel) {
            controller.state = it
        }
    }
}
