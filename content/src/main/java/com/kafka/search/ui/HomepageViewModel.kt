package com.kafka.search.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.booksByAuthor
import com.kafka.data.query.booksByCollection
import com.kafka.data.query.booksByKeyword
import com.kafka.domain.ObservableLoadingCounter
import com.kafka.domain.followed.ObserveFollowedItems
import com.kafka.domain.item.ObserveBatchItems
import com.kafka.language.domain.ObserveSelectedLanguages
import com.kafka.ui.actions.HomepageAction
import com.kafka.ui.search.HomepageViewState
import com.kafka.ui.search.SearchQuery
import com.kafka.ui.search.SearchQueryType
import com.kafka.ui_common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

val authors
    get() = listOf(
        "Shakespere",
        "Albert Camus",
        "Mirza Ghalib",
        "Franz Kafka",
        "Dostoyevsky",
        "Faiz Ahmed",
        "Ahmed Faraz"
    ).shuffled().take(2)

class HomepageViewModel @ViewModelInject constructor(
    private val observeBatchItems: ObserveBatchItems,
    observeFollowedItems: ObserveFollowedItems,
    private val loadingState: ObservableLoadingCounter,
    observeSelectedLanguages: ObserveSelectedLanguages
) : BaseViewModel<HomepageViewState>(HomepageViewState()) {
    val pendingActions = Channel<HomepageAction>(Channel.BUFFERED)

    init {
        viewModelScope.launch(Dispatchers.Main) {
            loadingState.observable
                .distinctUntilChanged()
                .collect {
                    setState { copy(isLoading = it) }
                }
        }

        viewModelScope.launchObserve(observeSelectedLanguages) { flow ->
            flow.distinctUntilChanged().execute { copy(selectedLanguages = it) }
        }

        viewModelScope.launchObserve(observeFollowedItems) { flow ->
            flow.distinctUntilChanged().execute { copy(favorites = it) }
        }

        observeSelectedLanguages(Unit)
        observeFollowedItems(Unit)

        viewModelScope.launchObserve(observeBatchItems) { flow ->
            flow.distinctUntilChanged().execute {
                copy(homepageItems = it)
            }
        }
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun submitQuery(searchQuery: SearchQuery) {
        setState { copy(query = searchQuery.text) }

        observeQuery(listOf(searchQuery.asArchiveQuery()))
    }

    fun updateHomepage() {
        observeQuery(authors.map { ArchiveQuery("Books by $it").booksByAuthor(it) })
    }

    private fun observeQuery(queries: List<ArchiveQuery>) {
        observeBatchItems(ObserveBatchItems.Params(queries))
    }
}

fun SearchQuery.asArchiveQuery() = ArchiveQuery().apply {
    when (type) {
        SearchQueryType.Creator -> booksByAuthor(text)
        SearchQueryType.Title -> booksByKeyword(text)
        SearchQueryType.Collection -> booksByCollection(text)
    }
}

