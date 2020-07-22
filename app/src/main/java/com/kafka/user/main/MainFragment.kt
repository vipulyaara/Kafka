package com.kafka.user.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kafka.content.ui.search.SearchViewModel
import com.kafka.ui.actions.ItemDetailAction
import com.kafka.ui.main.composeMainScreen
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.itemDetailDeepLinkUri
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            searchViewModel.actioner.observe { action ->
                when (action) {
                    is ItemDetailAction -> navController.navigate(itemDetailDeepLinkUri(action.item.itemId))
                }
            }
        }

        mainViewModel.updateLanguages()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            composeMainScreen(mainViewModel.liveData, searchViewModel.liveData, searchViewModel::submitAction)
        }
    }
}
