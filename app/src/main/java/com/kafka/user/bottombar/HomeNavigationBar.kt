package com.kafka.user.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.user.HomeNavigationItem
import com.kafka.user.HomeNavigationItems
import org.kafka.navigation.RootScreen
import ui.common.theme.theme.translucentSurfaceColor

internal object HomeNavigationBarDefaults {
    val colors
        @Composable
        get() = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.secondary,
            selectedTextColor = MaterialTheme.colorScheme.secondary,
            selectedIconColor = MaterialTheme.colorScheme.onSecondary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
        )
}

@Composable
internal fun HomeNavigationBar(
    selectedTab: RootScreen,
    onNavigationSelected: (RootScreen) -> Unit,
    modifier: Modifier = Modifier,
    isPlayerActive: Boolean = false,
) {
    val elevation = if (isPlayerActive) 0.dp else 8.dp
    val color = if (isPlayerActive) Color.Transparent else translucentSurfaceColor()
    val backgroundMod =
        if (isPlayerActive) Modifier.background(homeBottomNavigationGradient()) else Modifier

    NavigationBar(
        tonalElevation = elevation,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface),
        containerColor = color,
        windowInsets = WindowInsets.navigationBars,
        modifier = modifier.then(backgroundMod),
    ) {
        HomeNavigationItems.forEach { item ->
            NavigationBarItem(
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
private fun homeBottomNavigationGradient(color: Color = MaterialTheme.colorScheme.surface) =
    Brush.verticalGradient(
        listOf(
            color.copy(0.8f),
            color.copy(0.9f),
            color.copy(0.97f),
            color,
        )
    )

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
