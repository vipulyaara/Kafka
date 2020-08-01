package com.kafka.content.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kafka.content.R
import com.kafka.content.ui.search.*
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.itemDetailDeepLinkUri
import com.kafka.ui_common.onSearchIme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.view_search.*

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val searchController = SearchController()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(searchController)
            searchController.actioner = searchViewModel.actioner
        }

        searchViewModel.liveData.observe(viewLifecycleOwner, Observer {
            searchController.setData(it)
        })

        lifecycleScope.launchWhenCreated {
            searchViewModel.actioner.observe { action ->
                when (action) {
                    is ItemDetailAction -> navController.navigate(itemDetailDeepLinkUri(action.item.itemId))
                }
            }
        }

        etSearch.onSearchIme {
            searchViewModel.submitAction(
                SubmitQueryAction(
                    SearchQuery(
                        text = it
                    )
                )
            )
        }

        mainViewModel.updateLanguages()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}
