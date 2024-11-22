package com.kafka.library.favorites

import androidx.compose.runtime.Immutable
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.Item
import com.kafka.data.prefs.PreferencesStore
import com.kafka.domain.observers.account.ObserveUser
import com.kafka.domain.observers.library.ObserveFavorites
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import com.kafka.ui.components.item.LayoutType
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class FavoriteViewModel(
    observeFavorites: ObserveFavorites,
    preferencesStore: PreferencesStore,
    observeUser: ObserveUser,
    private val analytics: Analytics,
    private val navigator: Navigator,
) : ViewModel() {
    private val preferenceKey get() = stringPreferencesKey("layout")

    private val layoutType = preferencesStore.getStateFlow(
        keyName = preferenceKey, scope = viewModelScope, initialValue = LayoutType.Grid.name
    )

    val state: StateFlow<FavoriteViewState> = combine(
        observeFavorites.flow,
        layoutType.map { LayoutType.valueOf(it) },
        observeUser.flow
    ) { favorites, layout, user ->
        FavoriteViewState(
            favoriteItems = favorites,
            layoutType = layout,
            isUserLoggedIn = user != null
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FavoriteViewState(),
    )

    init {
        observeFavorites(Unit)
        observeUser(ObserveUser.Params())
    }

    fun updateLayoutType(layoutType: LayoutType) {
        this.layoutType.value = layoutType.name
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId = itemId, source = "favorites") }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    fun goToLogin() {
        navigator.navigate(Screen.Login)
    }
}

@Immutable
data class FavoriteViewState(
    val favoriteItems: List<Item>? = null,
    val layoutType: LayoutType = LayoutType.List,
    val isUserLoggedIn: Boolean = false,
)
