package com.kafka.ui.home

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.data.item.RowItems
import com.kafka.ui.colors
import com.kafka.ui.observe
import com.kafka.ui.setContentWithLifecycle
import com.kafka.ui.typography
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

fun ViewGroup.composeHomepageScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<HomepageViewState>,
    actioner: (HomepageAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromMdcTheme {
            HomepageScreen(viewState, actioner)
        }
    }
}

@Composable
private fun HomepageScreen(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    if (viewState.isLoading && viewState.items.isNullOrEmpty()) {
        Box(modifier = Modifier.padding(24.dp).fillMaxHeight().fillMaxWidth()) {
            CircularProgressIndicator()
        }
    } else {
        ContentList(viewState.items) { actioner(ContentItemClick(it)) }
    }
}

@Composable
fun ContentList(
    items: RowItems,
    actioner: (Item) -> Unit
) {
    val pair = items.values.flatten().run {
        subList(0, size/2).zip(subList(size/2, size))
    }
    VerticalScroller(modifier = Modifier.fillMaxWidth()) {
        Table(columns = 2) {
            pair.forEach {
                tableRow {
                    ContentItem(content = it.first, onItemClick = actioner)
                    ContentItem(content = it.second, onItemClick = actioner)
                }
            }
        }
    }
}

@Composable
fun Header(actioner: (HomepageAction) -> Unit) {
    Clickable(onClick = { actioner.invoke(SearchItemClick) }) {
        Text(
            modifier = Modifier.padding(top = 96.dp, bottom = 64.dp, start = 32.dp),
            text = "Search",
            style = typography().h5
        )
    }
}
