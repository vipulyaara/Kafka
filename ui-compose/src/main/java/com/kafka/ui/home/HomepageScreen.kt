package com.kafka.ui.home

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.AdapterList
import androidx.ui.graphics.RectangleShape
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.data.base.extensions.debug
import com.kafka.ui.actions.HomepageAction
import com.kafka.ui.actions.SubmitQueryAction
import com.kafka.ui.colors
import com.kafka.ui.search.HomepageViewState
import com.kafka.ui.search.widget.SearchView
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

fun ViewGroup.composeSearchScreen(
    homepageViewState: HomepageViewState,
    actioner: (HomepageAction) -> Unit
): Any = setContent(Recomposer.current()) {
    MaterialThemeFromMdcTheme {
        HomepageScreen(viewState = homepageViewState, actioner = actioner)
    }
}

@Composable
fun HomepageScreen(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (viewState.items.isNullOrEmpty()) actioner.invoke(SubmitQueryAction("Franz Kafka"))
        if (viewState.isLoading && viewState.items.isNullOrEmpty()) {
            FullScreenLoader()
        } else {
            Column {
                HomepageSearchView(actioner = actioner)
                Spacer(modifier = Modifier.height(24.dp))
                ContentResults(viewState = viewState, actioner = actioner)
            }
        }
    }
}

@Composable
fun HomepageSearchView(actioner: (HomepageAction) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalGravity = Alignment.CenterVertically
    ) {
        Card(color = colors().background, elevation = 1.dp, shape = RectangleShape) {
            SearchView(value = "Search for author", onSearch = { actioner(SubmitQueryAction(it)) })
        }
    }
}

@Composable
fun ContentResults(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    val items = viewState.items.values.flatten()
    debug { "Showing results for $items" }
    AdapterList(modifier = Modifier.fillMaxSize(), data = items) {
        ContentItem(content = it, onItemClick = {})
    }
}


@Composable
fun FullScreenLoader() {
    Stack(modifier = Modifier.padding(24.dp).fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.gravity(Alignment.Center))
    }
}
