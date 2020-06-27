package com.kafka.ui.home

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.Surface
import androidx.ui.unit.dp
import com.data.base.extensions.debug
import com.kafka.data.entities.Item
import com.kafka.ui.actions.HomepageAction
import com.kafka.ui.actions.ItemDetailAction
import com.kafka.ui.actions.SubmitQueryAction
import com.kafka.ui.actions.UpdateHomepageAction
import com.kafka.ui.alpha
import com.kafka.ui.colors
import com.kafka.ui.incrementTextSize
import com.kafka.ui.search.HomepageViewState
import com.kafka.ui.search.widget.SearchView
import com.kafka.ui.typography
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
        if (viewState.homepageItems.isNullOrEmpty()) actioner.invoke(UpdateHomepageAction())
        if (viewState.isLoading && viewState.homepageItems.isNullOrEmpty()) {
            FullScreenLoader()
        } else {
            Column {
                HomepageSearchView(actioner = actioner)
                VerticalScroller {
                    Column {
                        Surface(modifier = Modifier.fillMaxWidth()) {
                            ContentCarousal(list = viewState.favorites, actioner = actioner)
                        }
                        ContentResults(viewState = viewState, actioner = actioner)
                    }
                }
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
        SearchView(value = "Search for author", onSearch = { actioner(SubmitQueryAction(it)) })
    }
}

@Composable
fun ContentCarousal(list: List<Item>?, actioner: (HomepageAction) -> Unit) {
    HorizontalScroller(modifier = Modifier.padding(top = 12.dp)) {
        Row {
            Text(
                text = "FAVORITES",
                style = typography().h1.incrementTextSize(12).copy(color = colors().secondary.alpha(alpha = 0.3f)),
                modifier = Modifier.size(164.dp).padding(24.dp)
            )
            list?.forEach { ContentItem(content = it, onItemClick = { actioner(ItemDetailAction(it)) }) }
        }
    }
}

@Composable
fun ContentResults(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    val items = viewState.homepageItems
    debug { "Showing results for ${items.values.size}" }

    VerticalScroller {
        Column {
            items.values.map { it.items }.flatten().forEach {
                ContentItemList(content = it, onItemClick = { actioner(ItemDetailAction(it)) })
            }
        }
    }

//    AdapterList(
//        modifier = Modifier.padding(top = 24.dp),
//        data = items.values.map { it.items }.flatten() ?: arrayListOf()
//    ) {
//        ContentItemList(content = it, onItemClick = { actioner(ItemDetailAction(it)) })
//    }
}


@Composable
fun FullScreenLoader() {
    Stack(modifier = Modifier.padding(24.dp).fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.gravity(Alignment.Center))
    }
}
