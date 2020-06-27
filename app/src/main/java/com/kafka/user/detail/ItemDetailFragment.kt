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
import com.kafka.reader.Reader
import com.kafka.ui.detail.composeContentDetailScreen
import com.kafka.ui.player.Play
import com.kafka.ui_common.*
import com.kafka.user.extensions.openPdf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ItemDetailFragment : BaseFragment() {
    private val viewModel: ItemDetailViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToContentDetailAction.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(itemDetailDeepLinkUri(it.itemId))
        })

        viewModel.navigateToPlayerAction.observe(viewLifecycleOwner, EventObserver {
            playerViewModel.submitAction(Play(it))
            findNavController().navigate(playerDeepLinkUri())
        })

        viewModel.navigateToReaderAction.observe(viewLifecycleOwner, EventObserver {
            requireContext().openPdf(it ?: "")
//            findNavController().navigate(readerDeepLinkUri())
        })

        ItemDetailFragmentArgs.fromBundle(requireArguments()).let {
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
            composeContentDetailScreen(viewModel.viewState, viewModel::submitAction)
        }
    }
}
