package com.kafka.content.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kafka.content.R
import com.kafka.ui_common.action.RealActioner
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.setupToolbar
import com.kafka.ui_common.extensions.toggleNightMode
import com.kafka.ui_common.navigation.Navigation
import com.kafka.ui_common.navigation.navigate
import com.kafka.ui_common.ui.transitions.TransitionManager
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_item_detail.*

@AndroidEntryPoint
class ItemDetailFragment : BaseFragment() {
    private val viewModel: ItemDetailViewModel by viewModels()
    private val itemDetailController = ItemDetailController()
    private val pendingActions = RealActioner<ItemDetailAction>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.setupToolbar(R.menu.menu_master) {
            when (it?.itemId) {
                R.id.menu_dark_mode -> toolbar?.toggleNightMode(requireActivity(), it.itemId)
            }
        }

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
                    is ItemDetailAction.Read -> read(requireContext(), action.readerUrl, action.title)
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
            root?.transitionName = it
            viewModel.observeItemDetail(it)
            viewModel.updateItemDetail(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionManager.getMaterialContainerTransform().also {
            it.excludeTarget(toolbar, true)
            it.excludeChildren(toolbar, true)
        }
    }

    fun read(context: Context, readerUrl: String, title: String) {
        val config = ViewerConfig.Builder()
            .openUrlCachePath(context.cacheDir.absolutePath)
            .fullscreenModeEnabled(true)
            .multiTabEnabled(false)
            .documentEditingEnabled(false)
            .longPressQuickMenuEnabled(true)
            .toolbarTitle(title)
            .showSearchView(true)
            .build()
        DocumentActivity.openDocument(context, readerUrl.toUri(), config)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }
}
