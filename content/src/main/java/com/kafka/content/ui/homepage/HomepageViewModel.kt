package com.kafka.content.ui.homepage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.kafka.content.domain.homepage.GetHomepageTags
import com.kafka.content.domain.homepage.ObserveHomepage
import com.kafka.content.ui.query.asArchiveQuery
import com.kafka.data.model.launchObserve
import com.kafka.ui_common.base.ReduxViewModel
import com.kafka.ui_common.base.SnackbarManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomepageViewModel @ViewModelInject constructor(
    observeHomepage: ObserveHomepage,
    private val getHomepageTags: GetHomepageTags,
    snackbarManager: SnackbarManager
) : ReduxViewModel<HomepageViewState>(HomepageViewState()) {

    init {
        viewModelScope.launchObserve(observeHomepage) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(homepage = it) }
        }

        snackbarManager.launchInScope(viewModelScope) { uiError, visible ->
            viewModelScope.launchSetState {
                copy(error = if (visible) uiError else null)
            }
        }

        observeHomepage(selectedQuery.asArchiveQuery())
    }

    val selectedQuery
        get() = getHomepageTags(Unit).first { it.isSelected }.searchQuery
}
