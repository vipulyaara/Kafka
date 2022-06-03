package org.kafka.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.kafka.data.entities.Item
import org.kafka.common.extensions.rememberStateWithLifecycle
import org.kafka.common.widgets.DefaultScaffold
import org.kafka.common.widgets.LoadImage
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.material.TopBar
import org.kafka.ui_common_compose.shadowMaterial

@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel = hiltViewModel()) {
    val viewState by rememberStateWithLifecycle(stateFlow = viewModel.state)

    DefaultScaffold(topBar = { TopBar() }) {
        FavoriteItemGrid(viewState)
    }
}

@Composable
private fun FavoriteItemGrid(viewState: FavoriteViewState) {
    val navigator = LocalNavigator.current

    LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp)) {
        viewState.items?.let { items ->
            items(items, key = { it.itemId }) {
                FavoriteItem(item = it) {
                    navigator.navigate(LeafScreen.ItemDetail.createRoute(it))
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(item: Item, openItemDetail: (String) -> Unit) {
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
