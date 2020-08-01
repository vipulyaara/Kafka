package com.kafka.content.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.data.base.model.ArchiveQuery
import com.data.base.model.booksByAuthor
import com.data.base.model.booksByCollection
import com.data.base.model.booksByKeyword
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.content.domain.item.ObserveBatchItems
import com.kafka.content.domain.item.UpdateBatchItems
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.data.model.collectInto
import com.kafka.ui_common.action.RealActioner
import com.kafka.ui_common.base.ReduxViewModel
import com.kafka.ui_common.base.SnackbarManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    observeFollowedItems: ObserveFollowedItems,
    private val observeBatchItems: ObserveBatchItems,
    private val updateBatchItems: UpdateBatchItems,
    private val loadingState: ObservableLoadingCounter,
    snackbarManager: SnackbarManager
) : ReduxViewModel<SearchViewState>(SearchViewState()) {
    val actioner = RealActioner<HomepageAction>()

    init {
        viewModelScope.launchObserve(observeFollowedItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(favorites = it) }
        }

        viewModelScope.launchObserve(observeBatchItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(homepageItems = it) }
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
            actioner.observe {
                when (it) {
                    is UpdateHomepageAction -> updateHomepage()
                    is SubmitQueryAction -> submitQuery(it.query)
                }
            }
        }

        observeFollowedItems(Unit)
        updateHomepage()
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch { actioner.sendAction(action) }
    }

    private fun submitQuery(searchQuery: SearchQuery) {
        observeQuery(listOf(searchQuery.asArchiveQuery()))
    }

    private fun updateHomepage() {
        observeQuery(arrayOf("Franz Kafka", "Dostoyevsky").map {
            ArchiveQuery("Books by $it").booksByAuthor(it)
        })
    }

    private fun observeQuery(queries: List<ArchiveQuery>) {
        observeBatchItems(ObserveBatchItems.Params(queries))
        viewModelScope.launch {
            updateBatchItems(UpdateBatchItems.Params(queries)).collectInto(loadingState)
        }
    }
}

fun SearchQuery.asArchiveQuery() = ArchiveQuery().apply {
    when (type) {
        SearchQueryType.Creator -> booksByAuthor(text)
        SearchQueryType.Title -> booksByKeyword(text)
        SearchQueryType.Collection -> booksByCollection(text)
    }
}

