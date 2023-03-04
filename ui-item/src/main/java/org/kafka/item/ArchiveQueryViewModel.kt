package org.kafka.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import com.kafka.data.model.booksBySubject
import com.kafka.data.model.booksByTitleKeyword
import com.kafka.data.model.joinerOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.domain.interactors.UpdateItems
import org.kafka.domain.observers.ObserveQueryItems
import javax.inject.Inject

@HiltViewModel
class ArchiveQueryViewModel @Inject constructor(
    private val observeQueryItems: ObserveQueryItems,
    private val updateItems: UpdateItems,
    private val snackbarManager: SnackbarManager
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

    fun submitQuery(keyword: String, searchFilters: List<SearchFilter> = emptyList()) {
        val query = ArchiveQuery()
        searchFilters.forEach {
            val joiner = if (it == searchFilters.last()) "" else joinerOr
            when (it) {
                SearchFilter.Creator -> query.booksByAuthor(keyword, joiner)
                SearchFilter.Name -> query.booksByTitleKeyword(keyword, joiner)
                SearchFilter.Subject -> query.booksBySubject(keyword, joiner)
            }
        }
        submitQuery(query)
    }

    private fun submitQuery(query: ArchiveQuery) {
        observeQueryItems(ObserveQueryItems.Params(query))
        viewModelScope.launch {
            updateItems(UpdateItems.Params(query)).collectStatus(loadingState, snackbarManager)
        }
    }
}

enum class SearchFilter { Name, Creator, Subject }
