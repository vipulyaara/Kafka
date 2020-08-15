package com.kafka.content.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.data.base.model.ArchiveQuery
import com.kafka.content.domain.item.ObserveItems
import com.kafka.content.domain.item.UpdateItems
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.data.model.collectInto
import com.kafka.ui_common.base.ReduxViewModel
import com.kafka.ui_common.base.SnackbarManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val observeItems: ObserveItems,
    private val updateItems: UpdateItems,
    private val loadingState: ObservableLoadingCounter,
    snackbarManager: SnackbarManager
) : ReduxViewModel<SearchViewState>(SearchViewState()) {
    private val actioner = Channel<SearchAction>(Channel.BUFFERED)

    init {
        viewModelScope.launchObserve(observeItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { list ->
                copy(items = list)
            }
        }

        viewModelScope.launch {
            loadingState.observable.collectAndSetState { copy(isLoading = it) }
        }

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
    }

    fun submitAction(action: SearchAction) {
        viewModelScope.launch {
            if (!actioner.isClosedForSend) { actioner.send(action) }
        }
    }

    private fun submitQuery(searchQuery: SearchQuery) {
        observeQuery(searchQuery.asArchiveQuery())
    }

    private fun observeQuery(queries: ArchiveQuery) {
        observeItems(ObserveItems.Params(queries))
        viewModelScope.launch {
            updateItems(UpdateItems.Params(queries)).collectInto(loadingState)
        }
    }
}

