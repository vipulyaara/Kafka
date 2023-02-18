package com.kafka.user.bottombar.rail

import androidx.datastore.preferences.core.floatPreferencesKey
import com.sarahang.playback.core.PreferencesStore
import com.sarahang.playback.core.apis.PlayerEventLogger
import com.sarahang.playback.ui.sheet.ResizableLayoutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private val HomeNavigationRailDragOffsetKey = floatPreferencesKey("HomeNavigationRailWeightKey")

@HiltViewModel
class ResizableHomeNavigationRailViewModel @Inject constructor(
    preferencesStore: PreferencesStore,
    logger: PlayerEventLogger,
) : ResizableLayoutViewModel(
    preferencesStore = preferencesStore,
    logger = logger,
    preferenceKey = HomeNavigationRailDragOffsetKey,
    defaultDragOffset = 0f,
    analyticsPrefix = "home.navigationRail"
)
