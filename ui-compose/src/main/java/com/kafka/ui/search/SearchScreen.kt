package com.kafka.ui.search

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.Box
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.data.base.extensions.debug
import com.kafka.ui.*
import com.kafka.ui.actions.ItemClickAction
import com.kafka.ui.actions.SearchAction
import com.kafka.ui.actions.SubmitQueryAction
import com.kafka.ui.home.ContentList
import com.kafka.ui.search.widget.SearchView
import com.kafka.ui.widget.ButtonSmall
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

fun ViewGroup.composeSearchScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<SearchViewState>,
    actioner: (SearchAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    observe(state)?.let { MaterialThemeFromMdcTheme { SearchScreen(it, actioner) } }
}

@Composable
fun SearchScreen(viewState: SearchViewState, actioner: (SearchAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Filters(viewState)
        SearchView(viewState.query ?: "Meer") { actioner(SubmitQueryAction(it)) }

        debug { "${viewState.isLoading} ${viewState.items.isNullOrEmpty()}"}
        if (viewState.isLoading && viewState.items.values.flatten().isNullOrEmpty()) {
            Box(modifier = Modifier.padding(24.dp).fillMaxHeight().fillMaxWidth()) { CircularProgressIndicator() }
        } else {
            SearchResults(viewState = viewState, actioner = actioner)
        }
    }
}

@Composable
fun Filters(viewState: SearchViewState) {
    HorizontalScroller { Rows(viewState) }
}

@Composable
fun Rows(viewState: SearchViewState) {
    Row {
        viewState.selectedLanguages?.forEach {
            SelectionButton(it.languageName)
        }
    }
}

@Composable
fun SelectionButton(languageName: String) {
    ButtonSmall(
        modifier = Modifier.paddingHV(horizontal = 2.dp),
        backgroundColor = colors().background,
        border = Border(1.dp, colors().primary),
        shape = RoundedCornerShape(6.dp),
        text = languageName
    )
}

@Composable
fun SearchResults(viewState: SearchViewState, actioner: (SearchAction) -> Unit) {
    ContentList(items = viewState.items, actioner = { actioner(ItemClickAction(it)) })
}
