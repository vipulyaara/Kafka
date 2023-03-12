package org.kafka.library

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch
import org.kafka.common.ImmutableList
import org.kafka.common.extensions.alignCenter
import org.kafka.ui.components.pagerTabIndicatorOffset
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary

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
        divider = { Divider(color = MaterialTheme.colorScheme.surfaceVariant) },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        modifier = modifier,
    ) {
        tabs.items.forEachIndexed { index, season ->
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
        MaterialTheme.colorScheme.textPrimary
    }
    val style = if (selected) {
        MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    } else {
        MaterialTheme.typography.titleMedium
    }

    Text(
        modifier = Modifier
            .padding(Dimens.Spacing04)
            .fillMaxWidth(),
        text = title,
        style = style.alignCenter(),
        color = textColor
    )
}
