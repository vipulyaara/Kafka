package com.kafka.homepage.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.logger.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.RecentItem
import com.kafka.domain.interactors.recent.RemoveAllRecentItems
import com.kafka.domain.interactors.recent.RemoveRecentItem
import com.kafka.domain.observers.ObserveRecentItems
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecentViewModel @Inject constructor(
    observeRecentItems: ObserveRecentItems,
    private val removeAllRecentItems: RemoveAllRecentItems,
    private val removeRecentItem: RemoveRecentItem,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel() {
    val state = observeRecentItems.flow.map { recentItems ->
        RecentViewState(recentItems = recentItems.map { it.recentItem }.toPersistentList())
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = RecentViewState(),
    )

    init {
        observeRecentItems(ObserveRecentItems.Params(RECENT_ITEMS_LIMIT))
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId = itemId, source = "reading_list") }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    fun removeItem(fileId: String) {
        analytics.log { this.removeRecentItem() }
        viewModelScope.launch {
            removeRecentItem(fileId).collect()
        }
    }

    fun clearAllRecentItems() {
        analytics.log { this.clearRecentItems() }
        viewModelScope.launch {
            removeAllRecentItems(Unit).collect()
        }
    }
}

private const val RECENT_ITEMS_LIMIT = 50

data class RecentViewState(
    val recentItems: List<RecentItem> = persistentListOf(),
)
