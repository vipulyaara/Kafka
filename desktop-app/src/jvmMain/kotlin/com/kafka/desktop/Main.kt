package com.kafka.desktop

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kafka.shared.DesktopApplicationComponent
import com.kafka.shared.create
import ui.common.theme.theme.AppTheme

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

        AppTheme {
            component.mainScreen()
        }
    }
}
