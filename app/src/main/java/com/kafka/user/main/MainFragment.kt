package com.kafka.user.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kafka.search.ui.SearchViewModel
import com.kafka.ui.actions.ItemClickAction
import com.kafka.ui.home.ContentItemClick
import com.kafka.ui.main.composeMainScreen
import com.kafka.ui_common.BaseFragment
import com.kafka.ui_common.EventObserver
import com.kafka.ui_common.itemDetailDeepLinkUri
import com.kafka.user.home.HomepageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val homepageViewModel: HomepageViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.navigateToContentDetailAction.observe(viewLifecycleOwner, EventObserver {
            (it as? ItemClickAction)?.let { navController.navigate(itemDetailDeepLinkUri(it.item.itemId)) }
        })
        homepageViewModel.pendingActionLiveData.observe(viewLifecycleOwner, EventObserver {
            (it as? ContentItemClick)?.let { navController.navigate(itemDetailDeepLinkUri(it.item.itemId)) }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            composeMainScreen(
                searchViewModel.viewState,
                homepageViewModel.viewState,
                searchViewModel::submitAction,
                homepageViewModel::submitAction
            )
        }
    }
}
