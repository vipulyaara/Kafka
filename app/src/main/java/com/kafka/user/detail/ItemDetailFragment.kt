package com.kafka.user.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kafka.ui.content.composeContentDetailScreen
import com.kafka.user.common.BaseFragment
import com.kafka.user.home.HomepageFragmentDirections
import com.kafka.user.util.EventObserver
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 27/12/18.
 *
 * Fragment to host detail page.
 */
class ItemDetailFragment : BaseFragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ItemDetailViewModel by viewModels(factoryProducer = { viewModelFactory })

    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToContentDetailAction.observe(viewLifecycleOwner, EventObserver {
            navController.navigate(HomepageFragmentDirections.toContentDetail(it))
        })

        ItemDetailFragmentArgs.fromBundle(
            requireArguments()
        ).mvrxArg.let {
            viewModel.observeItemDetail(it)
            viewModel.updateItemDetail(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            composeContentDetailScreen(viewLifecycleOwner, viewModel.viewState, viewModel::submitAction)
        }
    }
}
