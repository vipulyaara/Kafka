package com.kafka.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.booksByAuthor
import com.kafka.data.query.booksByGenre
import com.kafka.domain.item.ObserveItems
import com.kafka.domain.item.UpdateItems
import com.kafka.domain.launchObserve
import com.kafka.ui.home.ContentItemClick
import com.kafka.ui.home.HomepageAction
import com.kafka.ui.home.HomepageViewState
import com.kafka.user.common.BaseViewModel
import com.kafka.user.util.Event
import com.kafka.user.util.ObservableLoadingCounter
import com.kafka.user.util.collectFrom
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for homepage.
 */
class HomepageViewModel @Inject constructor(
    observeItems: ObserveItems,
    private val loadingState: ObservableLoadingCounter,
    private val updateItems: UpdateItems
) : BaseViewModel<HomepageViewState>(HomepageViewState()) {

    private val queries = arrayListOf(
        ArchiveQuery().booksByAuthor("Kafka"),
        ArchiveQuery().booksByAuthor("Mark Twain"),
        ArchiveQuery().booksByGenre("Comedy")
    )

    private val pendingActions = Channel<HomepageAction>(Channel.BUFFERED)

    private val _navigateToContentDetailAction = MutableLiveData<Event<String>>()
    val navigateToContentDetailAction: LiveData<Event<String>>
        get() = _navigateToContentDetailAction

    init {
        viewModelScope.launch {
            loadingState.observable
                .distinctUntilChanged()
                .collect {
                    setState { copy(isLoading = it) }
                }
        }

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is ContentItemClick -> _navigateToContentDetailAction.postValue(Event(action.contentId))
            }
        }

        queries.map { ObserveItems.Params(it) }.forEach { params ->
            viewModelScope.launchObserve(observeItems) { flow ->
                flow.distinctUntilChanged().execute {
                    copy(items = it)
                }
            }

            observeItems(params)
        }
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun refresh() {
        queries.map { UpdateItems.Params(it) }.forEach {
            updateItems(it)
                .also { viewModelScope.launch { loadingState.collectFrom(it) } }
        }
    }
}

