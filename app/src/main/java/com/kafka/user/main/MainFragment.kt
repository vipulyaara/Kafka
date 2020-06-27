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
import com.kafka.search.ui.HomepageViewModel
import com.kafka.ui.actions.ItemDetailAction
import com.kafka.ui.actions.SubmitQueryAction
import com.kafka.ui.actions.UpdateHomepageAction
import com.kafka.ui.home.composeSearchScreen
import com.kafka.ui_common.BaseFragment
import com.kafka.ui_common.itemDetailDeepLinkUri
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val homepageViewModel: HomepageViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            for (action in homepageViewModel.pendingActions) when (action) {
                is UpdateHomepageAction -> homepageViewModel.updateHomepage()
                is SubmitQueryAction -> homepageViewModel.submitQuery(action.query)
                is ItemDetailAction -> navController.navigate(itemDetailDeepLinkUri(action.item.itemId))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            composeSearchScreen(homepageViewModel.viewState, homepageViewModel::submitAction)
        }
    }
}
