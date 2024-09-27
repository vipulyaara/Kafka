package com.kafka.user.home.bottombar.rail

import androidx.datastore.preferences.core.floatPreferencesKey
import com.sarahang.playback.core.PreferencesStore
import com.sarahang.playback.core.apis.PlayerEventLogger
import com.sarahang.playback.ui.sheet.ResizableLayoutViewModel
import javax.inject.Inject

private val HomeNavigationRailDragOffsetKey = floatPreferencesKey("HomeNavigationRailWeightKey")

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
