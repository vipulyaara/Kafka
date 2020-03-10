package com.kafka.user.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafka.data.content.ObserveContent
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.content.UpdateContent
import com.kafka.data.model.Event
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.booksByAuthor
import com.kafka.ui.home.ContentItemClick
import com.kafka.ui.home.HomepageAction
import com.kafka.ui.home.HomepageViewState
import com.kafka.user.feature.common.BaseViewModel
import com.kafka.user.ui.ObservableLoadingCounter
import com.kafka.user.ui.collectFrom
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
    observeContent: ObserveContent,
    private val loadingState: ObservableLoadingCounter,
    private val updateContent: UpdateContent
) : BaseViewModel<HomepageViewState>(HomepageViewState()) {

    private val query = ArchiveQuery().booksByAuthor("Kafka")

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
        viewModelScope.launchObserve(observeContent) { flow ->
            flow.distinctUntilChanged().execute {
                copy(items = it.data)
            }
        }

        observeContent(ObserveContent.Params(query))
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun refresh() {
        updateContent(UpdateContent.Params(query)).also {
            viewModelScope.launch {
                loadingState.collectFrom(it)
            }
        }
    }
}

