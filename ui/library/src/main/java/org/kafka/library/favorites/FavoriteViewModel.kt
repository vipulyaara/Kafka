package org.kafka.library.favorites

import androidx.compose.runtime.Immutable
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.Item
import com.kafka.data.prefs.PreferencesStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.kafka.analytics.logger.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.domain.observers.ObserveUser
import org.kafka.domain.observers.library.ObserveFavorites
import org.kafka.navigation.Navigator
import org.kafka.navigation.graph.Screen
import org.kafka.ui.components.item.LayoutType
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    observeFavorites: ObserveFavorites,
    preferencesStore: PreferencesStore,
    observeUser: ObserveUser,
    private val analytics: Analytics,
    private val navigator: Navigator,
) : ViewModel() {
    private val preferenceKey get() = stringPreferencesKey("layout")

    private val layoutType = preferencesStore.getStateFlow(
        keyName = preferenceKey, scope = viewModelScope, initialValue = LayoutType.List.name
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
