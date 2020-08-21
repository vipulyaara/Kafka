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
import com.kafka.ui_common.base.BaseFragment
import com.kafka.ui_common.extensions.onSearchIme
import com.kafka.ui_common.extensions.showSnackbar
import com.kafka.ui_common.navigation.Navigation
import com.kafka.ui_common.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_search_view.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    private val archiveQueryViewModel: ArchiveQueryViewModel by viewModels()
    private val searchController = SearchController()
    private val navController by lazy { findNavController() }
    private val searchActioner = Channel<SearchAction>(Channel.BUFFERED)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setController(searchController)
            searchController.searchActioner = searchActioner
        }

        archiveQueryViewModel.liveData.observe(viewLifecycleOwner, Observer {
            searchController.setData(it)
            it.error?.let { view.showSnackbar(it.message) }
            recyclerView.scrollToPosition(0)
        })

        lifecycleScope.launchWhenCreated {
            searchActioner.consumeAsFlow().collect { action ->
                when (action) {
                    is SearchAction.ItemDetailAction -> navController.navigate(Navigation.ItemDetail(action.item.itemId))
                    is SearchAction.SubmitQueryAction -> {
                        etSearch.setText(action.query.text)
                        etSearch.setSelection(etSearch.text.length)
                        archiveQueryViewModel.submitAction(action)
                    }
                }
            }
        }

//        lifecycleScope.launchWhenResumed {
//            etSearch.requestFocus()
//            requireContext().showKeyboard()
//        }

        etSearch.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launchWhenStarted {
                archiveQueryViewModel.onKeywordChanged(text.toString())
            }
        }

        etSearch.onSearchIme {
            lifecycleScope.launchWhenCreated {
                searchActioner.send(SearchAction.SubmitQueryAction(SearchQuery(it, SearchQueryType.TitleOrCreator)))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
}
