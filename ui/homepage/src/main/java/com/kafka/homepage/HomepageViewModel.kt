package com.kafka.homepage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.model.SearchFilter
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.showFeaturedItemLabels
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.kafka.analytics.logger.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.ObservableLoadingCounter
import com.kafka.common.UiMessageManager
import com.kafka.common.collectStatus
import com.kafka.common.shareText
import com.kafka.domain.interactors.UpdateHomepage
import com.kafka.domain.interactors.UpdateRecommendations
import com.kafka.domain.interactors.recent.RemoveRecentItem
import com.kafka.domain.observers.ObserveHomepage
import com.kafka.domain.observers.ObserveShareAppIndex
import com.kafka.domain.observers.ObserveUser
import com.kafka.navigation.Navigator
import com.kafka.navigation.deeplink.Config
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import com.kafka.homepage.R
import javax.inject.Inject

class HomepageViewModel @Inject constructor(
    observeHomepage: ObserveHomepage,
    observeUser: ObserveUser,
    observeShareAppIndex: ObserveShareAppIndex,
    private val updateHomepage: UpdateHomepage,
    private val removeRecentItem: RemoveRecentItem,
    private val updateRecommendations: UpdateRecommendations,
    private val navigator: Navigator,
    private val analytics: Analytics,
    private val loadingCounter: ObservableLoadingCounter,
    private val remoteConfig: RemoteConfig,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    val showCarouselLabels by lazy { remoteConfig.showFeaturedItemLabels() }

    val state: StateFlow<HomepageViewState> = combine(
        observeHomepage.flow,
        observeUser.flow,
        loadingCounter.observable,
        observeShareAppIndex.flow,
        uiMessageManager.message,
        ::HomepageViewState
    ).stateInDefault(scope = viewModelScope, initialValue = HomepageViewState())

    init {
        observeHomepage(Unit)
        observeUser(ObserveUser.Params())
        observeShareAppIndex(Unit)

        viewModelScope.launch {
            updateRecommendations(Unit).collect()
        }

        updateItems()
    }

    private fun updateItems() {
        viewModelScope.launch {
            updateHomepage(Unit).collectStatus(loadingCounter, uiMessageManager)
        }
    }

    fun retry() {
        viewModelScope.launch { uiMessageManager.clearMessage() }
        updateItems()
    }

    fun removeRecentItem(fileId: String) {
        viewModelScope.launch {
            analytics.log { removeRecentItem(fileId) }
            removeRecentItem.invoke(fileId).collect()
        }
    }

    fun openProfile() {
        navigator.navigate(Screen.Profile)
    }

    fun openItemDetail(itemId: String, collection: String?, source: String = "homepage") {
        analytics.log { openItemDetail(itemId = itemId, source = source, collection = collection) }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    fun openRecentItemDetail(itemId: String) {
        analytics.log { openRecentItem(itemId) }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    fun openSubject(name: String) {
        analytics.log { openSubject(name, "homepage") }
        navigator.navigate(Screen.Search(name, SearchFilter.Subject.name), RootScreen.Search)
    }

    fun openSearch() {
        navigator.navigate(Screen.Search(), RootScreen.Search)
    }

    fun openRecentItems() {
        analytics.log { this.openRecentItems() }
        navigator.navigate(Screen.RecentItems)
    }

    fun openCreator(name: String) {
        analytics.log { this.openCreator("homepage") }
        navigator.navigate(Screen.Search(name, SearchFilter.Creator.name), RootScreen.Search)
    }

    fun shareApp(context: Context) {
        analytics.log { this.shareApp() }
        context.shareText(context.getString(R.string.share_app_message, Config.PLAY_STORE_URL))
    }
}
