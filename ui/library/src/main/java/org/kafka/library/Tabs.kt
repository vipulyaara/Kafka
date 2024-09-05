package org.kafka.library

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.kafka.common.extensions.alignCenter
import org.kafka.ui.components.pagerTabIndicatorOffset
import ui.common.theme.theme.Dimens

@Composable
fun Tabs(
    pagerState: PagerState,
    tabs: ImmutableList<String>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(backgroundColor),
) {
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = backgroundColor,
        contentColor = contentColor,
        divider = { HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant) },
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        modifier = modifier,
    ) {
        tabs.forEachIndexed { index, season ->
            Tab(
                text = { TabItem(season, pagerState.currentPage == index) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(index) }
                }
            )
        }
    }
}

@Composable
private fun TabItem(title: String, selected: Boolean) {
    val textColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        LocalContentColor.current
    }

    Text(
        modifier = Modifier
            .padding(Dimens.Spacing04)
            .fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.titleMedium.alignCenter(),
        color = textColor
    )
}
