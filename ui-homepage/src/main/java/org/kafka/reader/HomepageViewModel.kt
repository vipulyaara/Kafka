package org.kafka.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.base.extensions.stateInDefault
import org.kafka.domain.interactors.GetHomepageTags
import org.kafka.domain.interactors.UpdateItems
import org.kafka.domain.interactors.asArchiveQuery
import org.kafka.domain.observers.ObserveHomepage
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    observeHomepage: ObserveHomepage,
    private val getHomepageTags: GetHomepageTags,
    private val updateItems: UpdateItems
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<HomepageViewState> = combine(
        observeHomepage.flow,
        loadingCounter.observable,
        uiMessageManager.message,
    ) { homepage, isLoading, message ->
        HomepageViewState(
            homepage = homepage,
            message = message,
            isLoading = isLoading
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = HomepageViewState(),
    )

    init {
        observeHomepage(selectedQuery)
        viewModelScope.launch {
            updateItems(UpdateItems.Params(selectedQuery))
                .collectStatus(loadingCounter, uiMessageManager)
        }
    }

    private val selectedQuery
        get() = getHomepageTags().first { it.isSelected }.searchQuery.asArchiveQuery()
}
