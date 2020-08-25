package com.kafka.content.ui.homepage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.content.domain.homepage.GetHomepageTags
import com.kafka.content.domain.homepage.HomepageTag
import com.kafka.content.domain.recent.ObserveRecentItems
import com.kafka.ui_common.base.ReduxViewModel
import com.kafka.ui_common.base.SnackbarManager
import kotlinx.coroutines.flow.distinctUntilChanged

class HomepageViewModel @ViewModelInject constructor(
    observeFollowedItems: ObserveFollowedItems,
    observeRecentItems: ObserveRecentItems,
    getHomepageTags: GetHomepageTags,
    snackbarManager: SnackbarManager
) : ReduxViewModel<HomepageViewState>(HomepageViewState(tags = getHomepageTags(Unit))) {

    init {
        viewModelScope.launchObserve(observeFollowedItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(favorites = it) }
        }

        viewModelScope.launchObserve(observeRecentItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(recentItems = it) }
        }

        snackbarManager.launchInScope(viewModelScope) { uiError, visible ->
            viewModelScope.launchSetState {
                copy(error = if (visible) uiError else null)
            }
        }

        observeFollowedItems(Unit)
        observeRecentItems(Unit)
    }

    fun getSelectedTag() = currentState().tags?.firstOrNull { it.isSelected }!!

    fun selectTag(homepageTag: HomepageTag) {
        currentState().apply {
            tags = tags.apply {
                this?.forEach { it.isSelected = false }
                this?.first { it.title == homepageTag.title }?.isSelected = true
            }
        }
    }
}
