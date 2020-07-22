package com.kafka.content.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kafka.ui.detail.ItemDetailAction
import com.kafka.ui.detail.composeContentDetailScreen
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.base.observeActions
import com.kafka.ui_common.itemDetailDeepLinkUri
import com.kafka.ui_common.playerDeepLinkUri
import com.kafka.ui_common.readerDeepLinkUri
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailFragment : BaseFragment() {
    private val viewModel: ItemDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.observeActions(viewModel.actioner) {
            when (it) {
                is ItemDetailAction.RelatedItemClick -> findNavController().navigate(itemDetailDeepLinkUri(it.item.itemId))
                is ItemDetailAction.Play -> findNavController().navigate(playerDeepLinkUri())
                is ItemDetailAction.Read -> findNavController().navigate(readerDeepLinkUri(it.readerUrl))
                else -> {}
            }
        }

        requireArguments().getString("item_id")?.let {
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
            composeContentDetailScreen(viewModel.liveData, viewModel::submitAction)
        }
    }
}
