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
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.snackbar.UiMessage
import org.kafka.common.UiMessageManager
import org.kafka.domain.observers.ObserveFavorites
import org.kafka.domain.observers.ObserveFollowedItems
import org.kafka.ui.components.item.LayoutType
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    observeFollowedItems: ObserveFollowedItems,
    observeFavorites: ObserveFavorites,
    preferencesStore: PreferencesStore
) : ViewModel() {
    private val preferenceKey get() = stringPreferencesKey("layout")
    private val uiMessageManager = UiMessageManager()

    private val layoutType = preferencesStore.getStateFlow(
        keyName = preferenceKey, scope = viewModelScope, initialValue = LayoutType.List.name
    )

    val state: StateFlow<FavoriteViewState> = combine(
        observeFavorites.flow,
        layoutType.map { LayoutType.valueOf(it) },
        uiMessageManager.message,
    ) { favorites, layout, message ->
        FavoriteViewState(
            favoriteItems = favorites,
            message = message,
            layoutType = layout
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FavoriteViewState(),
    )

    fun updateLayoutType(layoutType: LayoutType) {
        this.layoutType.value = layoutType.name
    }

    init {
        observeFavorites(Unit)
    }
}

@Immutable
data class FavoriteViewState(
    val favoriteItems: List<Item>? = null,
    val message: UiMessage? = null,
    val layoutType: LayoutType = LayoutType.List
)
