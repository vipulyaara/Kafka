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
import com.kafka.content.databinding.FragmentItemDetailBinding
import com.kafka.data.entities.ItemDetail
import com.kafka.ui_common.action.RealActioner
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.addScrollbarElevationView
import com.kafka.ui_common.extensions.shareText
import com.kafka.ui_common.extensions.showSnackbar
import com.kafka.ui_common.extensions.viewBinding
import com.kafka.ui_common.navigation.Navigation
import com.kafka.ui_common.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailFragment : BaseFragment() {
    private val binding by viewBinding(FragmentItemDetailBinding::bind)
    private val viewModel: ItemDetailViewModel by viewModels()
    private val itemDetailController = ItemDetailController()
    private val pendingActions = RealActioner<ItemDetailAction>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.recyclerView.apply {
            setController(itemDetailController)
            addScrollbarElevationView(binding.shadowView)
            itemDetailController.actioner = pendingActions
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            itemDetailController.setData(it)
            it.error?.let { view.showSnackbar(it) }
        })

        lifecycleScope.launchWhenCreated {
            pendingActions.observe { action ->
                when (action) {
                    is ItemDetailAction.RelatedItemClick -> findNavController().navigate(Navigation.ItemDetail(action.item.itemId))
                    is ItemDetailAction.Play -> {
                        findNavController().navigate(Navigation.Player(action.itemId))
                        viewModel.addRecentItem()
                    }
                    is ItemDetailAction.Read -> findNavController().navigate(Navigation.Reader(action.readerUrl))
                    is ItemDetailAction.Share -> {
                        requireContext().shareText(viewModel.shareItemText(action.itemId))
                    }
                    is ItemDetailAction.FavoriteClick -> {
                        viewModel.showFavoriteToast(requireContext())
                    }
                    else -> {
                    }
                }
            }
        }

        requireArguments().getString("item_id")?.let {
            viewModel.observeItemDetail(it)
            viewModel.updateItemDetail(it)
        }

        viewModel.setInitialData(
            ItemDetail(
                title = requireArguments().getString("title") ?: "",
                creator = requireArguments().getString("creator") ?: "",
                coverImage = requireArguments().getString("image_url") ?: ""
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }
}
