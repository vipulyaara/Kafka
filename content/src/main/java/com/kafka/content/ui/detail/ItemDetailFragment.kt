package com.kafka.content.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kafka.content.R
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.base.observeActions
import com.kafka.ui_common.itemDetailDeepLinkUri
import com.kafka.ui_common.playerDeepLinkUri
import com.kafka.ui_common.readerDeepLinkUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_item_detail.*

@AndroidEntryPoint
class ItemDetailFragment : BaseFragment() {
    private val viewModel: ItemDetailViewModel by viewModels()
    private val itemDetailController = ItemDetailController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(itemDetailController)
            itemDetailController.actioner = viewModel.actioner
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            itemDetailController.setData(it)
        })

        lifecycleScope.observeActions(viewModel.actioner) {
            when (it) {
                is ItemDetailAction.RelatedItemClick -> findNavController().navigate(itemDetailDeepLinkUri(it.item.itemId))
                is ItemDetailAction.Play -> findNavController().navigate(playerDeepLinkUri())
                is ItemDetailAction.Read -> findNavController().navigate(readerDeepLinkUri(it.readerUrl))
                else -> { }
            }
        }

        requireArguments().getString("item_id")?.let {
            viewModel.observeItemDetail(it)
            viewModel.updateItemDetail(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }
}
