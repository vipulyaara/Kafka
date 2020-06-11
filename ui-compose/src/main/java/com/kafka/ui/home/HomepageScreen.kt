package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.layout.Stack
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.data.base.extensions.debug
import com.kafka.data.entities.Item
import com.kafka.ui.typography

@Composable
fun FullScreenLoader() {
    Stack(modifier = Modifier.padding(24.dp).fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.gravity(Alignment.Center))
    }
}

@Composable
fun HomepageScreen(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    debug { "Homepage $viewState" }
    if (viewState.isLoading && viewState.items.isNullOrEmpty()) {
        FullScreenLoader()
    } else {
        ContentList(viewState.items) { actioner(ContentItemClick(it)) }
    }
}

@Composable
fun ContentList(
    items: List<Item>?,
    actioner: (Item) -> Unit
) {
//    VerticalScroller {
//            Column {
//                items?.forEach {
//                    ContentView(content = it, onItemClick = actioner)
//                }
//            }
//    }

    AdapterList(data = items ?: arrayListOf()) {
        ContentView(content = it, onItemClick = actioner)
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
