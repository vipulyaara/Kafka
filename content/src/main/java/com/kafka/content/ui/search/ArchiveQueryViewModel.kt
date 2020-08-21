package com.kafka.content.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.InvokeStatus
import com.data.base.launchObserve
import com.kafka.content.domain.item.ObserveItems
import com.kafka.content.domain.item.UpdateItems
import com.kafka.content.domain.search.AddRecentSearch
import com.kafka.content.domain.search.ObserveRecentSearch
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.ui_common.base.ReduxViewModel
import com.kafka.ui_common.base.SnackbarManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ArchiveQueryViewModel @ViewModelInject constructor(
    private val observeItems: ObserveItems,
    private val updateItems: UpdateItems,
    private val observeRecentSearch: ObserveRecentSearch,
    private val addRecentSearch: AddRecentSearch,
    private val loadingState: ObservableLoadingCounter,
    private val snackbarManager: SnackbarManager
) : ReduxViewModel<ArchiveQueryViewState>(ArchiveQueryViewState()) {
    private val actioner = Channel<SearchAction>(Channel.BUFFERED)
    private var recentSearches = listOf<String>()

    init {
        viewModelScope.launchObserve(observeItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { list ->
                copy(items = list)
            }
        }

        viewModelScope.launch { loadingState.observable.collectAndSetState { copy(isLoading = it) } }

        viewModelScope.launch { observeRecentSearch.observe().collect { recentSearches = it } }

        snackbarManager.launchInScope(viewModelScope) { uiError, visible ->
            viewModelScope.launchSetState {
                copy(error = if (visible) uiError else null)
            }
        }

        viewModelScope.launch {
            actioner.consumeAsFlow().collect {
                when (it) {
                    is SearchAction.SubmitQueryAction -> submitQuery(it.query)
                }
            }
        }

        observeRecentSearch(Unit)
    }

    suspend fun onKeywordChanged(keyword: String) {
        if (keyword.length > 1) {
            setState { copy(recentSearches = this@ArchiveQueryViewModel.recentSearches
                .filter { it.startsWith(keyword, true) }.distinct()) }
        } else {
            setState { copy(recentSearches = null) }
        }
    }

    fun submitAction(action: SearchAction) {
        viewModelScope.launch {
            if (!actioner.isClosedForSend) {
                actioner.send(action)
            }
        }
    }

    private fun submitQuery(searchQuery: SearchQuery) {
        val query = searchQuery.asArchiveQuery()
        addRecentSearch(searchQuery.text)

        observeItems(ObserveItems.Params(query))
        viewModelScope.launch {
            updateItems(UpdateItems.Params(query)).watchStatus()
        }
    }

    private fun Flow<InvokeStatus>.watchStatus() = viewModelScope.launch { collectStatus() }

    private suspend fun Flow<InvokeStatus>.collectStatus() = collect { status ->
        when (status) {
            InvokeStatus.Loading -> loadingState.addLoader()
            InvokeStatus.Success -> loadingState.removeLoader()
            is InvokeStatus.Error -> {
                snackbarManager.sendError(status.throwable)
                loadingState.removeLoader()
            }
        }
    }
}

