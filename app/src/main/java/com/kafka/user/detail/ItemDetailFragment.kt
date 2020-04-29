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
import com.kafka.player.ui.PlayerViewModel
import com.kafka.ui.detail.composeContentDetailScreen
import com.kafka.ui_common.BaseFragment
import com.kafka.ui_common.EventObserver
import com.kafka.user.common.itemDetailDeepLinkUri
import com.kafka.user.common.playerDeepLinkUri
import javax.inject.Inject

class ItemDetailFragment : BaseFragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ItemDetailViewModel by viewModels(factoryProducer = { viewModelFactory })
    private val playerViewModel: PlayerViewModel by viewModels(factoryProducer = { viewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToContentDetailAction.observe(viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(itemDetailDeepLinkUri(it.itemId))
            })

        viewModel.navigateToPlayerAction.observe(viewLifecycleOwner,
            EventObserver {
                playerViewModel.submitAction(it)
                findNavController().navigate(playerDeepLinkUri())
            })

        ItemDetailFragmentArgs.fromBundle(
            requireArguments()
        ).let {
            viewModel.imageResource = it.imageResource
            viewModel.observeItemDetail(it.itemId)
            viewModel.updateItemDetail(it.itemId)
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
