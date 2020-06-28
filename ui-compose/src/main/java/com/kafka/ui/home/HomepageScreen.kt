package com.kafka.ui.home

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.lifecycle.LiveData
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.Text
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.data.base.extensions.debug
import com.kafka.data.entities.Item
import com.kafka.ui.*
import com.kafka.ui.R
import com.kafka.ui.actions.HomepageAction
import com.kafka.ui.actions.ItemDetailAction
import com.kafka.ui.actions.UpdateHomepageAction
import com.kafka.ui.search.HomepageViewState
import com.kafka.ui.search.widget.HomepageSearchView
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

const val searchHint = "Search..."

fun ViewGroup.composeSearchScreen(
    homepageViewState: LiveData<HomepageViewState>,
    actioner: (HomepageAction) -> Unit
): Any = setContent(Recomposer.current()) {
    val viewState = observe(homepageViewState)
    MaterialThemeFromMdcTheme {
        if (viewState != null) {
            HomepageScreen(viewState = viewState, actioner = actioner)
        }
    }
}

@Composable
fun HomepageScreen(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (viewState.homepageItems.isNullOrEmpty()) actioner.invoke(UpdateHomepageAction())
        if (viewState.isLoading) {
            FullScreenLoader()
        } else {
            Column {
                HomepageSearchView(viewState = viewState, actioner = actioner)
                ContentResults(viewState = viewState, actioner = actioner)
            }
        }
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

    Stack {
        LazyColumnItems(
            items = items.values.map { it.items }.flatten()
        ) {
            ContentItemList(content = it, onItemClick = { actioner(ItemDetailAction(it)) })
        }
        Shadow()
    }
}


@Composable
fun FullScreenLoader() {
    Stack(modifier = Modifier.padding(24.dp).fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.gravity(Alignment.Center))
    }
}

@Composable
fun Shadow() {
    CoilImage(
        data = R.drawable.img_shadow_top_to_bottom,
        modifier = Modifier.fillMaxWidth().height(16.dp)
    )
}
