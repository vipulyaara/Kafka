package com.kafka.content.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kafka.content.R
import com.kafka.content.databinding.FragmentSearchBinding
import com.kafka.content.ui.query.ArchiveQueryViewModel
import com.kafka.content.ui.query.SearchAction
import com.kafka.content.ui.query.SearchAction.SubmitQueryAction.Companion.titleOrCreatorAction
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.onSearchIme
import com.kafka.ui_common.extensions.showSnackbar
import com.kafka.ui_common.extensions.viewBinding
import com.kafka.ui_common.navigation.Navigation
import com.kafka.ui_common.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val archiveQueryViewModel: ArchiveQueryViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchController = SearchController()
    private val navController by lazy { findNavController() }
    private val searchActioner = Channel<SearchAction>(Channel.BUFFERED)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            setController(searchController)
            searchController.searchActioner = searchActioner
        }

        archiveQueryViewModel.liveData.observe(viewLifecycleOwner, Observer {
            searchController.setData(it)
            it.error?.let { view.showSnackbar(it.message) }
            binding.recyclerView.scrollToPosition(0)
        })

        lifecycleScope.launchWhenCreated {
            searchActioner.consumeAsFlow().collect { action ->
                when (action) {
                    is SearchAction.ItemDetailAction -> navController.navigate(Navigation.ItemDetail(action.item.itemId))
                    is SearchAction.SubmitQueryAction -> {
                        binding.searchView.etSearch.setText(action.query.text)
                        binding.searchView.etSearch.setSelection(binding.searchView.etSearch.text.length)
                        archiveQueryViewModel.submitQuery(action.query)
                        searchViewModel.addRecentSearch(action.query)
                    }
                }
            }
        }

        binding.searchView.etSearch.doOnTextChanged { text, _, _, _ ->
            searchViewModel.onKeywordChanged(text.toString())
        }

        binding.searchView.etSearch.onSearchIme {
            lifecycleScope.launchWhenCreated { searchActioner.send(titleOrCreatorAction(it)) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
}
