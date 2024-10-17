package com.kafka.desktop.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.kafka.common.adaptive.windowWidthSizeClass
import com.kafka.desktop.MainScreen
import com.kafka.homepage.HomepageViewModel
import com.kafka.navigation.graph.RootScreen
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.HazeScaffold
import me.tatarka.inject.annotations.Inject
import ui.common.theme.theme.Dimens

typealias HomeNavigation = @Composable () -> Unit

@Inject
@Composable
internal fun HomeNavigation(
    modifier: Modifier = Modifier,
    mainScreen: MainScreen
) {
    val selectedTab  = HomeNavigationItems[0].rootScreen
    val windowSizeClass = windowWidthSizeClass()
    val navigationType = remember(windowWidthSizeClass()) {
        NavigationType.forWindowSizeSize(windowSizeClass)
    }

    HazeScaffold(
        bottomBar = {
        },
        blurBottomBar = navigationType == NavigationType.BOTTOM_NAVIGATION,
        modifier = modifier,
    ) { paddings ->
        ProvideScaffoldPadding(paddings) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                HomeNavigationDrawer(
                    selectedNavigation = selectedTab,
                    navigationItems = HomeNavigationItems,
                    onNavigationSelected = {

                    },
                    modifier = Modifier.fillMaxHeight(),
                ) {

                }

                VerticalDivider(color = MaterialTheme.colorScheme.surfaceContainer)

                mainScreen()
            }
        }
    }
}

@Composable
private fun HomeNavigationDrawer(
    selectedNavigation: RootScreen,
    navigationItems: List<HomeNavigationItem>,
    onNavigationSelected: (RootScreen) -> Unit,
    modifier: Modifier = Modifier,
    extraContent: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .widthIn(max = 200.dp)
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.Spacing12)
                .background(MaterialTheme.colorScheme.surface)
                .width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing08)
        ) {
            for (item in navigationItems) {
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = if (selectedNavigation == item.rootScreen) item.selectedImageVector else item.iconImageVector,
                            contentDescription = null,
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    label = {
                        Text(
                            text = item.labelResId,
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    selected = selectedNavigation == item.rootScreen,
                    onClick = { onNavigationSelected(item.rootScreen) },
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimens.Radius08))
                )
            }
        }

        extraContent()
    }
}

@Composable
internal fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = rememberVectorPainter(item.iconImageVector)
    val selectedPainter = rememberVectorPainter(item.selectedImageVector)

    Icon(
        painter = if (selected) selectedPainter else painter,
        contentDescription = null,
    )
}

internal enum class NavigationType {
    BOTTOM_NAVIGATION,
    RAIL,
    PERMANENT_DRAWER;

    companion object {
        fun forWindowSizeSize(
            windowSizeClass: WindowWidthSizeClass
        ): NavigationType = PERMANENT_DRAWER
    }
}
