package org.kafka.homepage

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
import org.kafka.analytics.logger.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.common.shareText
import org.kafka.domain.interactors.UpdateHomepage
import org.kafka.domain.interactors.UpdateRecommendations
import org.kafka.domain.interactors.recent.RemoveRecentItem
import org.kafka.domain.observers.ObserveHomepage
import org.kafka.domain.observers.ObserveShareAppIndex
import org.kafka.domain.observers.ObserveUser
import org.kafka.navigation.Navigator
import org.kafka.navigation.deeplink.Config
import org.kafka.navigation.graph.RootScreen
import org.kafka.navigation.graph.Screen
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
