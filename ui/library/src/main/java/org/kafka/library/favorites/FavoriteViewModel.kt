package org.kafka.library.favorites

import androidx.compose.runtime.Immutable
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.Item
import com.kafka.data.prefs.PreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.kafka.analytics.logger.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.domain.observers.library.ObserveFavorites
import org.kafka.navigation.Navigator
import org.kafka.navigation.Screen
import org.kafka.ui.components.item.LayoutType
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    observeFavorites: ObserveFavorites,
    preferencesStore: PreferencesStore,
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
    ) { favorites, layout ->
        FavoriteViewState(favoriteItems = favorites, layoutType = layout)
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FavoriteViewState(),
    )

    fun updateLayoutType(layoutType: LayoutType) {
        this.layoutType.value = layoutType.name
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId) }
        navigator.navigate(Screen.ItemDetail.createRoute(navigator.currentRoot.value, itemId))
    }

    init {
        observeFavorites(Unit)
    }
}

@Immutable
data class FavoriteViewState(
    val favoriteItems: List<Item>? = null,
    val layoutType: LayoutType = LayoutType.List
)
