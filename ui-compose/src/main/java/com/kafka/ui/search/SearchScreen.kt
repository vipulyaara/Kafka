package com.kafka.ui.search

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp
import com.data.base.extensions.debug
import com.kafka.data.entities.Language
import com.kafka.ui.actions.ItemClickAction
import com.kafka.ui.actions.SearchAction
import com.kafka.ui.actions.SubmitQueryAction
import com.kafka.ui.alignCenter
import com.kafka.ui.colors
import com.kafka.ui.home.ContentList
import com.kafka.ui.paddingHV
import com.kafka.ui.search.widget.SearchView

@Composable
fun SearchScreen(viewState: SearchViewState, actioner: (SearchAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SearchView(viewState.query ?: "Search for an author...") { actioner(SubmitQueryAction(it)) }
        Filters(viewState)

        debug { "${viewState.isLoading} ${viewState.items.isNullOrEmpty()}" }
        if (viewState.isLoading && viewState.items.values.flatten().isNullOrEmpty()) {
            Box(modifier = Modifier.padding(24.dp).fillMaxHeight().fillMaxWidth()) { CircularProgressIndicator() }
        } else {
            SearchResults(viewState = viewState, actioner = actioner)
        }
    }
}

@Composable
fun Filters(viewState: SearchViewState) {
    HorizontalScroller(modifier = Modifier.paddingHV(horizontal = 16.dp, vertical = 12.dp)) { FilterItem(viewState) }
}

@Composable
fun FilterItem(viewState: SearchViewState) {
    val selectedNavigation = androidx.compose.state { viewState.selectedLanguages?.firstOrNull() }
    Row {
        viewState.selectedLanguages?.forEach {
            SelectionButton(it, selectedNavigation)
        }
    }
}

@Composable
fun SelectionButton(language: Language, selected: MutableState<Language?>) {
    Clickable(onClick = { selected.value = language }) {
        Card(
            modifier = Modifier.paddingHV(horizontal = 2.dp),
            color = if (selected.value == language) colors().primary else colors().surface,
            border = Border(1.5.dp, colors().onPrimary),
            shape = RoundedCornerShape(2.dp),
            elevation = 2.dp
        ) {
            Text(
                modifier = Modifier.paddingHV(horizontal = 12.dp, vertical = 4.dp),
                text = language.languageName,
                style = MaterialTheme.typography.body1.alignCenter(),
                maxLines = 1
            )
        }
    }
}

@Composable
fun SearchResults(viewState: SearchViewState, actioner: (SearchAction) -> Unit) {
    ContentList(items = viewState.items, actioner = { actioner(ItemClickAction(it)) })
}
