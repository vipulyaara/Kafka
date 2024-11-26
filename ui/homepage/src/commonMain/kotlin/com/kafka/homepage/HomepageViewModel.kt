package com.kafka.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.UiMessageManager
import com.kafka.common.platform.ShareUtils
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.domain.interactors.UpdateHomepage
import com.kafka.domain.interactors.recent.RemoveRecentItem
import com.kafka.domain.observers.ObserveHomepage
import com.kafka.domain.observers.ObserveRecentItems
import com.kafka.domain.observers.ObserveShareAppIndex
import com.kafka.domain.observers.account.ObserveUser
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import com.kafka.navigation.graph.Screen.ItemDetail.Origin
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class HomepageViewModel(
    observeHomepage: ObserveHomepage,
    observeUser: ObserveUser,
    observeShareAppIndex: ObserveShareAppIndex,
    observeRecentItems: ObserveRecentItems,
    private val updateHomepage: UpdateHomepage,
    private val removeRecentItem: RemoveRecentItem,
    private val navigator: Navigator,
    private val analytics: Analytics,
    private val shareUtils: ShareUtils,
    private val snackbarManager: SnackbarManager,
    private val uiMessageManager: UiMessageManager,
) : ViewModel() {
    val recentItems = observeRecentItems.flow.stateInDefault(viewModelScope, emptyList())

    val state: StateFlow<HomepageViewState> = combine(
        observeHomepage.flow,
        observeUser.flow,
        updateHomepage.inProgress,
        observeShareAppIndex.flow,
        uiMessageManager.message,
        ::HomepageViewState
    ).stateInDefault(scope = viewModelScope, initialValue = HomepageViewState())

    init {
        observeHomepage(Unit)
        observeUser(ObserveUser.Params())
        observeShareAppIndex(Unit)
        observeRecentItems(ObserveRecentItems.Params(10))

        updateItems()
    }

    private fun updateItems() {
        viewModelScope.launch {
            updateHomepage(Unit).onException {
                uiMessageManager.emitMessage(UiMessage("Failed to update Homepage"))
                snackbarManager.addMessage("Failed to update Homepage")
            }
        }
    }

    fun retry() {
        viewModelScope.launch { uiMessageManager.clearMessage() }
        updateItems()
    }

    fun removeRecentItem(itemId: String) {
        viewModelScope.launch {
            analytics.log { removeRecentItem(itemId) }
            removeRecentItem.invoke(itemId)
        }
    }

    fun openProfile() {
        navigator.navigate(Screen.Profile)
    }

    fun openItemDetail(
        itemId: String,
        origin: Origin = Origin.Unknown,
        source: String = "homepage"
    ) {
        val originKey = if (origin == Origin.Unknown) "Homepage" else origin.name
        analytics.log { openItemDetail(itemId = itemId, source = source, origin = originKey) }
        navigator.navigate(Screen.ItemDetail(itemId = itemId, origin = origin))
    }

    fun openRecentItemDetail(itemId: String) {
        analytics.log { openRecentItem(itemId) }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    fun openSubject(name: String) {
        analytics.log { openSubject(name, "homepage") }
        navigator.navigate(Screen.Search(name), RootScreen.Search)
    }

    fun openSearch() {
        navigator.navigate(Screen.Search(), RootScreen.Search)
    }

    fun openRecentItems() {
        analytics.log { this.openRecentItems() }
        navigator.navigate(Screen.RecentItems)
    }

    fun openCreator(name: String) {
        analytics.log { this.openCreator(name = name, source = "homepage") }
        navigator.navigate(Screen.Search(name), RootScreen.Search)
    }

    fun shareApp(text: String, context: Any?) {
        analytics.log { this.shareApp() }
        shareUtils.shareText(text = text, context = context)
    }
}
