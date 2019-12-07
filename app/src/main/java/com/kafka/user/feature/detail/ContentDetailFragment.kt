package com.kafka.user.feature.detail

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.kafka.user.R
import com.kafka.user.databinding.FragmentItemDetailBinding
import com.kafka.user.feature.common.BaseDataBindingFragment
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 27/12/18.
 *
 * Fragment to host detail page.
 */
class ContentDetailFragment : BaseDataBindingFragment<FragmentItemDetailBinding>(
    R.layout.fragment_item_detail
) {
    @Inject
    lateinit var viewModelFactory: ContentDetailViewModel.Factory
    @Inject lateinit var controller: ContentDetailController
    private val viewModel: ContentDetailViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHome.apply {
            setController(controller)
        }
    }

    override fun invalidate() {
        withState(viewModel) {
            controller.state = it
        }
    }
}
