package com.kafka.content.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.content.domain.item.ObserveBatchItems
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.data.model.RowItems
import com.data.base.model.ArchiveQuery
import com.data.base.model.booksByAuthor
import com.data.base.model.booksByCollection
import com.data.base.model.booksByKeyword
import com.kafka.language.domain.ObserveSelectedLanguages
import com.kafka.player.domain.CommandPlayer
import com.kafka.player.domain.ObservePlayer
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

class HomepageViewModel @ViewModelInject constructor(
    observePlayer: ObservePlayer,
    commandPlayer: CommandPlayer,
    observeFollowedItems: ObserveFollowedItems,
    observeSelectedLanguages: ObserveSelectedLanguages,
    private val observeBatchItems: ObserveBatchItems,
    private val loadingState: ObservableLoadingCounter
) : BaseViewModel<HomepageViewState>(HomepageViewState()) {
    val pendingActions = Channel<HomepageAction>(Channel.BUFFERED)

    init {
        viewModelScope.launch(Dispatchers.Main) {
            loadingState.observable
                .distinctUntilChanged()
                .collect { setState { copy(isLoading = it) } }
        }

        viewModelScope.launchObserve(observeSelectedLanguages) { flow ->
            flow.distinctUntilChanged().execute { copy(selectedLanguages = it) }
        }

        viewModelScope.launchObserve(observeFollowedItems) { flow ->
            flow.distinctUntilChanged().execute { copy(favorites = it) }
        }

        observeSelectedLanguages(Unit)
        observeFollowedItems(Unit)
        updateHomepage()

        viewModelScope.launchObserve(observeBatchItems) { flow ->
            flow.distinctUntilChanged().execute {
                copy(homepageItems = it)
            }
        }

        viewModelScope.launchObserve(observePlayer) {
            it.execute { copy(playerData = it, playerCommand = { commandPlayer(it) }) }
        }

        observePlayer(Unit)
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun submitQuery(searchQuery: SearchQuery) {
        setState { copy(query = searchQuery.text, homepageItems = RowItems()) }
        observeQuery(listOf(searchQuery.asArchiveQuery()))
    }

    fun updateHomepage() {
        observeQuery(arrayOf("Franz Kafka", "Dostoyevsky").map { ArchiveQuery("Books by $it")
            .booksByAuthor(it) })
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

