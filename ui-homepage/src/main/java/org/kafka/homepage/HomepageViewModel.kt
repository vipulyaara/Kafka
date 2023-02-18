package org.kafka.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.domain.interactors.GetHomepageTags
import org.kafka.domain.interactors.RemoveRecentItem
import org.kafka.domain.interactors.UpdateItems
import org.kafka.domain.interactors.asArchiveQuery
import org.kafka.domain.observers.ObserveHomepage
import org.kafka.domain.observers.ObserveUser
import org.kafka.navigation.Navigator
import org.kafka.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    observeHomepage: ObserveHomepage,
    private val getHomepageTags: GetHomepageTags,
    private val updateItems: UpdateItems,
    private val removeRecentItem: RemoveRecentItem,
    observeUser: ObserveUser,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<HomepageViewState> = combine(
        observeHomepage.flow,
        observeUser.flow,
        loadingCounter.observable,
        uiMessageManager.message,
    ) { homepage, user, isLoading, message ->
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
        observeHomepage(selectedQuery)
        observeUser(Unit)

        viewModelScope.launch {
            updateItems(UpdateItems.Params(selectedQuery))
                .collectStatus(loadingCounter, uiMessageManager)
        }
    }

    fun removeRecentItem(itemId: String) {
        viewModelScope.launch {
            analytics.log { this.removeRecentItem(itemId) }
            removeRecentItem.invoke(itemId).collect()
        }
    }

    fun loginClicked() {
        analytics.log { this.loginClicked() }
        navigator.navigate(Screen.Login.createRoute(navigator.currentRoot.value))
    }

    fun openProfile() {
        viewModelScope.launch {
            analytics.log { this.logoutClicked() }
            navigator.navigate(Screen.Profile.createRoute(navigator.currentRoot.value))
        }
    }

    private val selectedQuery
        get() = getHomepageTags().first { it.isSelected }.searchQuery.asArchiveQuery()
}
