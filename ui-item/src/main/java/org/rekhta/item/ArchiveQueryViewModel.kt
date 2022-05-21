package org.rekhta.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.model.ArchiveQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.rekhta.base.debug
import org.rekhta.base.extensions.stateInDefault
import org.rekhta.domain.interactors.SearchQuery
import org.rekhta.domain.interactors.UpdateItems
import org.rekhta.domain.interactors.asArchiveQuery
import org.rekhta.domain.observers.ObserveQueryItems
import javax.inject.Inject

@HiltViewModel
class ArchiveQueryViewModel @Inject constructor(
    private val observeQueryItems: ObserveQueryItems,
    private val updateItems: UpdateItems
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<ArchiveQueryViewState> = combine(
        observeQueryItems.flow,
        loadingState.observable,
        uiMessageManager.message,
    ) { items, isLoading, message ->
        ArchiveQueryViewState(
            items = items,
            message = message,
            isLoading = isLoading
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ArchiveQueryViewState(),
    )

    fun submitQuery(searchQuery: SearchQuery) {
        submitQuery(searchQuery.asArchiveQuery())
    }

    fun submitQuery(query: ArchiveQuery) {
        observeQueryItems(ObserveQueryItems.Params(query))
        viewModelScope.launch {
            updateItems(UpdateItems.Params(query)).collectStatus(loadingState, uiMessageManager)
        }
    }
}
