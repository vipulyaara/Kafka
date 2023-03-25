package org.kafka.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.base.debug
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.domain.interactors.UpdateHomepage
import org.kafka.domain.interactors.recent.RemoveRecentItem
import org.kafka.domain.observers.ObserveHomepage
import org.kafka.domain.observers.ObserveUser
import org.kafka.navigation.Navigator
import org.kafka.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    observeHomepage: ObserveHomepage,
    private val updateHomepage: UpdateHomepage,
    private val removeRecentItem: RemoveRecentItem,
    observeUser: ObserveUser,
    private val navigator: Navigator,
    private val analytics: Analytics
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<HomepageViewState> = combine(
        observeHomepage.flow,
        observeUser.flow,
        loadingCounter.observable,
        uiMessageManager.message,
    ) { homepage, user, isLoading, message ->
        debug { "User is $user $message ${homepage.recentItems} ${homepage.queryItems}" }
        HomepageViewState(
            homepage = homepage,
            user = user,
            message = message,
            isLoading = isLoading
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = HomepageViewState(),
    )

    init {
        observeHomepage(Unit)
        observeUser(ObserveUser.Params())

        updateItems()
    }

    private fun updateItems() {
        viewModelScope.launch {
            updateHomepage(Unit)
                .collectStatus(loadingCounter, uiMessageManager)
        }
    }

    fun retry() {
        viewModelScope.launch { uiMessageManager.clearMessage() }
        updateItems()
    }

    fun removeRecentItem(itemId: String) {
        viewModelScope.launch {
            analytics.log { this.removeRecentItem(itemId) }
            removeRecentItem.invoke(itemId).collect()
        }
    }

    fun openLogin() {
        analytics.log { this.loginClicked() }
        navigator.navigate(Screen.Login.createRoute(navigator.currentRoot.value))
    }

    fun openProfile() {
        viewModelScope.launch {
            navigator.navigate(Screen.Profile.createRoute(navigator.currentRoot.value))
        }
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId, "homepage") }
        navigator.navigate(Screen.ItemDetail.createRoute(navigator.currentRoot.value, itemId))
    }

    fun openRecentItemDetail(itemId: String) {
        analytics.log { this.openRecentItem(itemId) }
        navigator.navigate(Screen.ItemDetail.createRoute(navigator.currentRoot.value, itemId))
    }
}
