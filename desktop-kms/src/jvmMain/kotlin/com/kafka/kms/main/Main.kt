package com.kafka.kms.main

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kafka.kms.components.theme.AppleTheme
import ui.common.theme.theme.isDark

fun main() = application {
    Window(
        title = "KMS",
        state = rememberWindowState(placement = WindowPlacement.Fullscreen),
        onCloseRequest = ::exitApplication,
    ) {
        val applicationComponent = remember {
            KmsApplicationComponent::class.create()
        }

        val component = remember(applicationComponent) {
            KmsWindowComponent::class.create(applicationComponent)
        }

        component.appInitializers.forEach { it.init() }

        val isDarkTheme = applicationComponent.preferencesStore.isDark()

        AppleTheme(isDarkTheme) {
            component.kmsHomepage()
        }
    }
}