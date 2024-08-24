package com.kafka.user.home.bottombar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.kafka.common.test.testTagUi
import org.kafka.navigation.graph.RootScreen

internal object HomeNavigationBarDefaults {
    val colors
        @Composable
        get() = NavigationBarItemDefaults.colors()
}

@Composable
internal fun HomeNavigationBar(
    selectedTab: RootScreen,
    onNavigationSelected: (RootScreen) -> Unit,
    modifier: Modifier = Modifier,
    isPlayerActive: Boolean = false,
) {
    NavigationBar(
        tonalElevation = if (isPlayerActive) 0.dp else 8.dp,
        windowInsets = WindowInsets.navigationBars,
        containerColor = NavigationBarDefaults.containerColor,
        modifier = modifier,
    ) {
        HomeNavigationItems.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.testTagUi("home_tab_${item.labelResId}"),
                selected = selectedTab == item.rootScreen,
                onClick = { onNavigationSelected(item.rootScreen) },
                icon = {
                    HomeNavigationItemIcon(
                        item = item,
                        selected = selectedTab == item.rootScreen
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.labelResId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                alwaysShowLabel = true,
                colors = HomeNavigationBarDefaults.colors,
            )
        }
    }
}

@Composable
internal fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = when (item) {
        is HomeNavigationItem.ImageVectorIcon -> rememberVectorPainter(item.iconImageVector)
    }
    val selectedPainter = item.selectedImageVector?.let { rememberVectorPainter(it) }

    Icon(
        painter = if (selected) (selectedPainter ?: painter) else painter,
        contentDescription = stringResource(item.contentDescriptionResId),
    )
}
