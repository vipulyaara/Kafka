package org.kafka.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.kafka.data.entities.Item
import org.kafka.common.UiMessage
import org.kafka.common.asImmutable
import org.kafka.common.shadowMaterial
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.LoadImage
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator

@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel = hiltViewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    val navigator = LocalNavigator.current
    val openItemDetail: (String) -> Unit = {
        navigator.navigate(LeafScreen.ItemDetail.createRoute(it))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState()

        Tabs(
            pagerState = pagerState,
            tabs = listOf("Favorites", "Downloaded").asImmutable(),
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalPager(modifier = Modifier.fillMaxSize(), count = 2, state = pagerState) {
            when (it) {
                0 -> FavoriteItemGrid(favoriteItems = viewState.favoriteItems, openItemDetail)
                1 -> DownloadedItemGrid(downloadedItems = viewState.downloadedItems, openItemDetail)
            }
        }
    }
}

@Composable
private fun FavoriteItemGrid(favoriteItems: List<Item>, openItemDetail: (String) -> Unit) {

    if (favoriteItems.isEmpty()) {
        FullScreenMessage(
            uiMessage = UiMessage(
                title = "No favorite items",
                message = "The items you favorite will appear here"
            )
        )
    } else {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(favoriteItems, key = { it.itemId }) {
                LibraryItem(item = it, openItemDetail = openItemDetail)
            }
        }
    }
}

@Composable
private fun DownloadedItemGrid(downloadedItems: List<Item>, openItemDetail: (String) -> Unit) {
    if (downloadedItems.isEmpty()) {
        FullScreenMessage(
            uiMessage = UiMessage(
                title = "No downloaded items",
                message = "The items you download will appear here"
            )
        )
    } else {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(downloadedItems, key = { it.itemId }) {
                LibraryItem(item = it, openItemDetail = openItemDetail)
            }
        }
    }
}

@Composable
private fun LibraryItem(item: Item, openItemDetail: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openItemDetail(item.itemId) }
            .padding(4.dp)
            .shadowMaterial(12.dp, RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LoadImage(
            data = item.coverImage,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Column(
            modifier = Modifier
                .padding(12.dp)
                .height(72.dp)
        ) {
            Text(
                text = item.title.orEmpty(),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.creator?.name.orEmpty(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

