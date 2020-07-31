package com.kafka.ui.home

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.Text
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.data.base.extensions.debug
import com.kafka.data.entities.Item
import com.kafka.ui.*
import com.kafka.ui.R
import com.kafka.ui.actions.HomepageAction
import com.kafka.ui.actions.ItemDetailAction
import com.kafka.ui.search.SearchViewState
import com.kafka.ui.search.widget.HomepageSearchView
import dev.chrisbanes.accompanist.coil.CoilImage

const val searchHint = "Search..."

@Composable
fun SearchScreen(viewState: SearchViewState, actioner: (HomepageAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (viewState.isLoading && viewState.homepageItems.isEmpty()) {
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
fun Banner() {
    Card(
        modifier = Modifier.height(232.dp).fillMaxWidth().padding(24.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 0.dp
    ) {
        CoilImage(data = R.drawable.img_banner_11, contentScale = ContentScale.Crop)
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
fun ContentResults(viewState: SearchViewState, actioner: (HomepageAction) -> Unit) {
    val items = viewState.homepageItems
    debug { "Showing results for ${items.values.size}" }

    Stack {
        LazyColumnItems(
            items = items.values.map { it.items }.flatten()
        ) {
            ContentItemList(content = it, onItemClick = { actioner(ItemDetailAction(it)) })
        }
    }
}

@Composable
fun Authors() {
    HorizontalScroller {
        Row {
            arrayListOf(
                "Franz Kafka",
                "Dostoyevsky",
                "Pablo Picasso",
                "Mirza Ghalib",
                "Mir Taqi Mir",
                "Albert Camus",
                "Faiz Ahmed Faiz"
            ).map { Author(it) }.forEach {
                AuthorItem(author = it)
            }
        }
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
        modifier = Modifier.fillMaxWidth().height(24.dp)
    )
}
