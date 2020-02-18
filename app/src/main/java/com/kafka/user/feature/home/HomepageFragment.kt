package com.kafka.user.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kafka.data.model.EventObserver
import com.kafka.ui.home.composeHomepageScreen
import com.kafka.user.feature.common.BaseFragment
import com.kafka.user.feature.detail.contentId
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 02/02/19.
 */

class HomepageFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HomepageViewModel by viewModels(factoryProducer = { viewModelFactory })

    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToContentDetailAction.observe(viewLifecycleOwner, EventObserver {
            contentId = it
            navController.navigate(HomepageFragmentDirections.toPoetDetail(it))
        })

        viewModel.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            composeHomepageScreen(
                viewLifecycleOwner,
                viewModel.viewState,
                viewModel::submitAction
            )
        }
    }
}
