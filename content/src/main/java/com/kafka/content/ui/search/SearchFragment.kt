package com.kafka.content.ui.search

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
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchController = SearchController()
    private val navController by lazy { findNavController() }
    private val searchActioner = Channel<SearchAction>(Channel.BUFFERED)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(searchController)
            searchController.searchActioner = searchActioner
        }

        searchViewModel.liveData.observe(viewLifecycleOwner, Observer {
            searchController.setData(it)
        })

        lifecycleScope.launchWhenCreated {
            searchActioner.consumeAsFlow().collect { action ->
                when (action) {
                    is SearchAction.ItemDetailAction -> navController.navigate(Navigation.ItemDetail(action.item.itemId))
                    is SearchAction.SubmitQueryAction -> { searchViewModel.submitAction(action) }
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
}
