package com.kafka.user.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.data.query.languages
import com.kafka.data.query.queries
import com.kafka.domain.item.ObserveBatchItems
import com.kafka.language.domain.UpdateLanguages
import com.kafka.ui.home.ContentItemClick
import com.kafka.ui.home.HomepageAction
import com.kafka.ui.home.HomepageViewState
import com.kafka.ui.home.SearchItemClick
import com.kafka.ui_common.BaseComposeViewModel
import com.kafka.ui_common.Event
import com.kafka.ui_common.isLoading
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomepageViewModel @ViewModelInject constructor(
    updateLanguages: UpdateLanguages,
    observeBatchItems: ObserveBatchItems
) : BaseComposeViewModel<HomepageViewState>(HomepageViewState()) {
    private val pendingActions = Channel<HomepageAction>(Channel.BUFFERED)
    val pendingActionLiveData = MutableLiveData<Event<HomepageAction>>()
    val navigateToSearchAction = MutableLiveData<Event<HomepageAction>>()

    init {
        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is ContentItemClick -> pendingActionLiveData.postValue(Event(action))
                is SearchItemClick -> pendingActionLiveData.postValue(Event(action))
            }
        }

        viewModelScope.launchObserve(observeBatchItems) { flow ->
            flow.distinctUntilChanged().execute {
                isLoading = it.isLoading()
                it.dataOrNull()?.let { items = it }
            }
        }

        observeBatchItems(ObserveBatchItems.Params(queries))

        updateLanguages(UpdateLanguages.Params(languages))

//        searchViewModel.updateItems(queries)
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun updateItems() {
//        searchViewModel.updateItems(queries)
    }
}
