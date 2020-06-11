package com.kafka.ui.search

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.Tab
import androidx.ui.material.TabRow
import androidx.ui.unit.dp
import com.data.base.extensions.debug
import com.kafka.ui.actions.ItemClickAction
import com.kafka.ui.actions.SearchAction
import com.kafka.ui.actions.SubmitQueryAction
import com.kafka.ui.alpha
import com.kafka.ui.colors
import com.kafka.ui.home.ContentList
import com.kafka.ui.paddingHV
import com.kafka.ui.typography

@Composable
fun SearchScreen(viewState: SearchViewState, actioner: (SearchAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        actioner.invoke(SubmitQueryAction("Franz Kafka"))

        debug { "${viewState.isLoading} ${viewState.items.isNullOrEmpty()}" }
        if (viewState.isLoading && viewState.items.isNullOrEmpty()) {
            Stack(modifier = Modifier.padding(24.dp).fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.gravity(Alignment.Center))
            }
        } else {
            Stack(modifier = Modifier) {
                SearchResults(viewState = viewState, actioner = actioner)
            }
        }
    }
}

@Composable
fun ContentTabs(tabList: Array<String>, onSelect: (String) -> Unit) {
    val selectedIndex = state { 0 }

    TabRow(
        modifier = Modifier.height(1.dp),
        items = tabList.toList(),
        backgroundColor = Color.Transparent,
        scrollable = true,
        selectedIndex = selectedIndex.value
    ) { position, tabData ->
        Tab(
            modifier = Modifier.wrapContentWidth(),
            selected = selectedIndex.value == position,
            onSelected = {
                selectedIndex.value = position
                onSelect(tabList[selectedIndex.value])
            }) {
            val isSelected = selectedIndex.value == position
            val alpha = if (isSelected) 1f else 0.4f
            Text(
                text = tabData ?: "",
                modifier = Modifier.paddingHV(horizontal = 16.dp, vertical = 16.dp),
                style = typography().body2.copy(color = colors().onPrimary.alpha(alpha = alpha)),
                maxLines = 1
            )
        }
    }
}

@Composable
fun SearchResults(viewState: SearchViewState, actioner: (SearchAction) -> Unit) {
    ContentList(items = viewState.items, actioner = { actioner(ItemClickAction(it)) })
}
