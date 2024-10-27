package com.kafka.homepage.recent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.data.entities.RecentItem
import com.kafka.domain.interactors.GetRecentItems
import com.kafka.domain.interactors.recent.RemoveAllRecentItems
import com.kafka.domain.interactors.recent.RemoveRecentItem
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecentItemsViewModel @Inject constructor(
    getRecentItems: GetRecentItems,
    private val removeAllRecentItems: RemoveAllRecentItems,
    private val removeRecentItem: RemoveRecentItem,
    private val snackbarManager: SnackbarManager,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel() {
    var recentItems by mutableStateOf<List<RecentItem>>(emptyList())
    val loading = getRecentItems.inProgress.stateInDefault(viewModelScope, false)

    init {
        viewModelScope.launch {
            getRecentItems(GetRecentItems.Params(RECENT_ITEMS_LIMIT))
                .onSuccess { recentItems = it }
                .onException { snackbarManager.addMessage(it.message.orEmpty()) }
        }
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId = itemId, source = "reading_list") }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    fun removeItem(fileId: String) {
        analytics.log { this.removeRecentItem() }
        viewModelScope.launch {
            removeRecentItem(fileId)
        }
    }

    fun clearAllRecentItems() {
        analytics.log { this.clearRecentItems() }
        viewModelScope.launch {
            removeAllRecentItems(Unit)
        }
    }
}

private const val RECENT_ITEMS_LIMIT = 100
