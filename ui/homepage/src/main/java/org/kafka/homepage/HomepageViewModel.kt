package org.kafka.homepage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.Item
import com.kafka.data.model.SearchFilter
import com.kafka.data.model.homepage.HomepageBanner
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isRecommendationRowEnabled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.analytics.logger.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.domain.interactors.UpdateHomepage
import org.kafka.domain.interactors.recent.RemoveRecentItem
import org.kafka.domain.interactors.recommendation.GetRecommendedContent
import org.kafka.domain.observers.ObserveHomepage
import org.kafka.domain.observers.ObserveUser
import org.kafka.navigation.Navigator
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    observeHomepage: ObserveHomepage,
    observeUser: ObserveUser,
    private val updateHomepage: UpdateHomepage,
    private val removeRecentItem: RemoveRecentItem,
    private val getRecommendedContent: GetRecommendedContent,
    private val navigator: Navigator,
    private val analytics: Analytics,
    private val loadingCounter: ObservableLoadingCounter,
    private val remoteConfig: RemoteConfig
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    var recommendedContent by mutableStateOf(emptyList<Item>())

    val state: StateFlow<HomepageViewState> = combine(
        observeHomepage.flow,
        observeUser.flow,
        loadingCounter.observable,
        uiMessageManager.message,
        ::HomepageViewState
    ).stateInDefault(scope = viewModelScope, initialValue = HomepageViewState())

    init {
        observeHomepage(Unit)
        observeUser(ObserveUser.Params())

        viewModelScope.launch {
            if (remoteConfig.isRecommendationRowEnabled()) {
                recommendedContent = getRecommendedContent(Unit).getOrNull().orEmpty()
            }
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

    fun removeRecentItem(itemId: String) {
        viewModelScope.launch {
            analytics.log { removeRecentItem(itemId) }
            removeRecentItem.invoke(itemId).collect()
        }
    }

    fun openProfile() {
        navigator.navigate(Screen.Profile.createRoute(navigator.currentRoot.value))
    }

    fun openItemDetail(itemId: String) {
        analytics.log { openItemDetail(itemId, "homepage") }
        navigator.navigate(Screen.ItemDetail.createRoute(navigator.currentRoot.value, itemId))
    }

    fun openRecentItemDetail(itemId: String) {
        analytics.log { openRecentItem(itemId) }
        navigator.navigate(Screen.ItemDetail.createRoute(navigator.currentRoot.value, itemId))
    }

    fun openSubject(name: String) {
        analytics.log { openSubject(name, "homepage") }
        navigator.navigate(
            Screen.Search.createRoute(
                root = RootScreen.Search,
                keyword = name,
                filter = SearchFilter.Subject.name
            )
        )
    }

    fun openSearch() {
        navigator.navigate(Screen.Search.createRoute(RootScreen.Search))
    }

    fun openRecentItems() {
        analytics.log { this.openRecentItems() }
        navigator.navigate(Screen.RecentItems.createRoute(RootScreen.Home))
    }

    fun onBannerClick(banner: HomepageBanner) {
        when (banner.action) {
            HomepageBanner.Action.Search -> navigator.navigate(
                Screen.Search.createRoute(RootScreen.Search, banner.keyword)
            )

            HomepageBanner.Action.ItemDetail -> navigator.navigate(
                Screen.ItemDetail.createRoute(navigator.currentRoot.value, banner.keyword.orEmpty())
            )
        }
    }
}
