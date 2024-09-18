package org.kafka.homepage.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.RecentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kafka.analytics.logger.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.domain.interactors.recent.RemoveAllRecentItems
import org.kafka.domain.interactors.recent.RemoveRecentItem
import org.kafka.domain.observers.ObserveRecentItems
import org.kafka.navigation.Navigator
import org.kafka.navigation.graph.Screen
import javax.inject.Inject

@HiltViewModel
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
        observeRecentItems(Unit)
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

data class RecentViewState(
    val recentItems: List<RecentItem> = persistentListOf(),
)
