package org.kafka.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.HomepageBanner
import com.kafka.data.model.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.base.domain.InvokeSuccess
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.domain.interactors.UpdateHomepage
import org.kafka.domain.interactors.account.LogoutUser
import org.kafka.domain.interactors.recent.RemoveRecentItem
import org.kafka.domain.observers.ObserveHomepage
import org.kafka.domain.observers.ObserveUser
import org.kafka.navigation.Navigator
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
import javax.inject.Inject
import org.kafka.common.snackbar.UiMessage.Companion as SnackbarUiMessage

@HiltViewModel
class HomepageViewModel @Inject constructor(
    observeHomepage: ObserveHomepage,
    private val updateHomepage: UpdateHomepage,
    private val removeRecentItem: RemoveRecentItem,
    private val logoutUser: LogoutUser,
    observeUser: ObserveUser,
    private val navigator: Navigator,
    private val analytics: Analytics,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

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
            analytics.log { this.removeRecentItem(itemId) }
            removeRecentItem.invoke(itemId).collect()
        }
    }

    fun openLogin() {
        analytics.log { this.loginClicked() }
        navigator.navigate(Screen.Login.createRoute(navigator.currentRoot.value))
    }

    fun openFeedback() {
        viewModelScope.launch {
            navigator.navigate(Screen.Feedback.createRoute(navigator.currentRoot.value))
        }
    }

    fun logout(onLogout: () -> Unit = { navigator.goBack() }) {
        viewModelScope.launch {
            analytics.log { logoutClicked() }
            logoutUser(Unit).collectStatus(loadingCounter, snackbarManager) { status ->
                if (status == InvokeSuccess) {
                    snackbarManager.addMessage(SnackbarUiMessage(R.string.logged_out))
                    onLogout()
                }
            }
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

    fun openSubject(name: String) {
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
