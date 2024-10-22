package com.kafka.desktop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme
import com.kafka.navigation.rememberBottomSheetNavigator
import com.kafka.shared.injection.DesktopApplicationComponent
import com.kafka.shared.injection.WindowComponent
import com.kafka.shared.injection.create
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.shouldUseDarkColors

fun main() = application {
    Window(
        title = "Kafka",
        state = rememberWindowState(placement = WindowPlacement.Fullscreen),
        onCloseRequest = ::exitApplication,
    ) {
        val applicationComponent = remember {
            DesktopApplicationComponent::class.create()
        }

        val component = remember(applicationComponent) {
            WindowComponent::class.create(applicationComponent)
        }

        component.appInitializers.forEach { it.init() }

        val theme by applicationComponent.preferencesStore.observeTheme()
            .collectAsStateWithLifecycle(Theme.SYSTEM)
        val bottomSheetNavigator = rememberBottomSheetNavigator()
        val navController = rememberNavController(bottomSheetNavigator)

        AppTheme(
            isDarkTheme = applicationComponent.preferencesStore.shouldUseDarkColors()
        ) {
            component.mainScreen(navController, bottomSheetNavigator, theme)
        }
    }
}
