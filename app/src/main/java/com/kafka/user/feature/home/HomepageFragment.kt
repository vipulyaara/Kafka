package com.kafka.user.feature.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.data.extensions.d
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
            onScrolled {
                binding.elevationShadow.isActivated = canScrollVertically(-1)
            }
        }

        viewModel.navigateToContentDetailAction.observe(this,
            EventObserver { content -> navController.navigate(toPoetDetail(content.contentId)) }
        )

        controller.callbacks = viewModel
        viewModel.refresh()
    }

    override fun invalidate() {
        withState(viewModel) {
            d { "homepage invalidate "}
            controller.state = it
            binding.rvHome.scrollToPosition(0)
        }
    }
}
