package com.kafka.user.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.data.entities.Item
import com.kafka.data.entities.Language
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.ResultTye
import com.kafka.data.query.booksByAuthor
import com.kafka.domain.item.ObserveBatchItems
import com.kafka.language.domain.UpdateLanguages
import com.kafka.search.ui.SearchViewModel
import com.kafka.ui.home.ContentItemClick
import com.kafka.ui.home.HomepageAction
import com.kafka.ui.home.HomepageViewState
import com.kafka.ui.home.SearchItemClick
import com.kafka.ui_common.BaseViewModel
import com.kafka.ui_common.Event
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomepageViewModel @Inject constructor(
    private val searchViewModel: SearchViewModel,
    private val updateLanguages: UpdateLanguages,
    observeBatchItems: ObserveBatchItems
) : BaseViewModel<HomepageViewState>(HomepageViewState()) {

    private val queries = arrayListOf(
        ArchiveQuery("Franz Kafka").booksByAuthor("Kafka"),
        ArchiveQuery("Mark Twain").booksByAuthor("Mark Twain"),
        ArchiveQuery("Dostoyevsky").booksByAuthor("dostoyevsky"),
        ArchiveQuery("Mirza Ghalib").booksByAuthor("Ghalib").copy(resultTye = ResultTye.Banner)
    ).mapIndexed { index, archiveQuery ->  archiveQuery.copy(position = index) }

    private val languages = arrayListOf(
        Language("en", "English"),
        Language("hi", "Hindi"),
        Language("ur", "Urdu"),
        Language("de", "German"),
        Language("fr", "French")
    )

    private val pendingActions = Channel<HomepageAction>(Channel.BUFFERED)
    val navigateToContentDetailAction = MutableLiveData<Event<Item>>()
    val navigateToSearchAction = MutableLiveData<Event<Boolean>>()

    init {
        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is ContentItemClick -> navigateToContentDetailAction.postValue(Event(action.item))
                is SearchItemClick -> navigateToSearchAction.postValue(Event(true))
            }
        }

        viewModelScope.launchObserve(observeBatchItems) { flow ->
            flow.distinctUntilChanged().execute { items ->
                items.let { copy(items = it) }
            }
        }

        observeBatchItems(ObserveBatchItems.Params(queries))

        updateLanguages(UpdateLanguages.Params(languages))
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun updateItems() {
        searchViewModel.updateItems(queries)
    }
}
