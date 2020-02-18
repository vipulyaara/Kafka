package com.kafka.user.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafka.data.data.config.ProcessLifetime
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.entities.Content
import com.kafka.data.feature.content.ContentRepository
import com.kafka.data.feature.content.ObserveContent
import com.kafka.data.feature.content.UpdateContent
import com.kafka.data.model.Event
import com.kafka.data.model.RailItem
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.booksByAuthor
import com.kafka.data.util.AppCoroutineDispatchers
import com.kafka.ui.home.ContentItemClick
import com.kafka.ui.home.HomepageAction
import com.kafka.ui.home.HomepageViewState
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.feature.common.BaseMvRxViewModel
import com.kafka.user.feature.common.BaseViewModel
import com.kafka.user.ui.ObservableLoadingCounter
import com.kafka.user.ui.collectFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseMvRxViewModel] to provide data for homepage.
 */
class HomepageViewModel @Inject constructor(
    private val loadingState: ObservableLoadingCounter,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val contentRepository: ContentRepository
) : BaseViewModel<HomepageViewState>(HomepageViewState()) {

    private val creators = arrayOf("Kafka", "Sherlock", "Mark")

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

        creators.forEach {
            val observeContent = ObserveContent(appCoroutineDispatchers, contentRepository)
            viewModelScope.launchObserve(observeContent) { flow ->
                flow.distinctUntilChanged().execute {
                    onItemsFetched(it())
                }
            }

            observeContent(ObserveContent.Params(ArchiveQuery().booksByAuthor(it)))
        }
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun refresh() {
        creators.forEach {
            val updateContent =
                UpdateContent(appCoroutineDispatchers, processScope, contentRepository)
            updateContent(UpdateContent.Params.ByCreator(it)).also {
                viewModelScope.launch {
                    loadingState.collectFrom(it)
                }
            }
        }
    }

    private fun HomepageViewState.onItemsFetched(list: List<Content>?): HomepageViewState {

        val new =
            items?.toMutableSet()
                .also {
                    it?.clear()
                    it?.add(RailItem("Books by Kafka", list))
                }
        new?.map {
            it.contents?.map {
                it.also { it.coverImageResource = getRandomAuthorResource() }
            }
        }
        return copy(items = new)
    }
}

