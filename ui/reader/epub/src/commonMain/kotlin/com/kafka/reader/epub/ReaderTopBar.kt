@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.kafka.common.extensions.getContext
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconResource
import com.kafka.common.widgets.shadowMaterial
import com.kafka.navigation.LocalNavigator
import com.kafka.reader.epub.components.SettingsState
import com.kafka.reader.epub.components.TocState
import com.kafka.reader.epub.settings.ReaderTheme
import com.kafka.ui.components.material.TopBar
import ui.common.theme.theme.Dimens

@Composable
fun ReaderTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    settingsState: SettingsState,
    tocState: TocState,
    theme: ReaderTheme,
    shareItem: () -> Unit,
) {
    val navigator = LocalNavigator.current

    TopBar(
        modifier = Modifier
            .fillMaxWidth()
            .shadowMaterial(Dimens.Elevation12),
        containerColor = theme.backgroundColor,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            ActionIcon(
                icon = Icons.Back,
                contentColor = theme.contentColor,
                contentDescription = "Back",
                onClick = navigator::goBack
            )
        },
        actions = {
            ActionIcon(
                icon = Icons.Share,
                contentColor = theme.contentColor,
                contentDescription = "Share",
                onClick = shareItem
            )

            ActionIcon(
                icon = Icons.List,
                contentColor = theme.contentColor,
                contentDescription = "Table of contents",
                onClick = { tocState.show() }
            )

            ActionIcon(
                icon = Icons.Settings,
                contentColor = theme.contentColor,
                contentDescription = "Reader Settings",
                onClick = { settingsState.show() }
            )
        },
    )
}

@Composable
private fun ActionIcon(
    icon: ImageVector,
    contentColor: Color,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        IconResource(
            imageVector = icon,
            tint = contentColor,
            contentDescription = contentDescription
        )
    }
}
