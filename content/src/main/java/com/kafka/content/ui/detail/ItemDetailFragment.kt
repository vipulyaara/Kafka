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
import com.kafka.ui_common.Navigation
import com.kafka.ui_common.action.RealActioner
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_item_detail.*

@AndroidEntryPoint
class ItemDetailFragment : BaseFragment() {
    private val viewModel: ItemDetailViewModel by viewModels()

    //    private val readerViewModel: ReaderViewModel by viewModels()
    private val itemDetailController = ItemDetailController()
    private val pendingActions = RealActioner<ItemDetailAction>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(itemDetailController)
            itemDetailController.actioner = pendingActions
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            itemDetailController.setData(it)
        })

        lifecycleScope.launchWhenCreated {
            pendingActions.observe { action ->
                when (action) {
                    is ItemDetailAction.RelatedItemClick -> findNavController().navigate(Navigation.ItemDetail(action.item.itemId))
                    is ItemDetailAction.Play -> findNavController().navigate(Navigation.Player(action.itemId))
                    is ItemDetailAction.Read -> {
//                        readerViewModel.read(requireContext(), action.readerUrl)
                    }
                    is ItemDetailAction.FavoriteClick -> {
                        viewModel.sendAction(action)
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }

    override fun onBackPressed(): Boolean {
        super.onBackPressed()
        findNavController().popBackStack()
        return true
    }
}
